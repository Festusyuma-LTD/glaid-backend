package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.AddressRequest
import festusyuma.com.glaid.model.Address
import festusyuma.com.glaid.model.Location
import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.repository.AddressRepo
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.LocationRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AddressService(
        private val userService: UserService,
        private val locationRepo: LocationRepo,
        private val addressRepo: AddressRepo,
        private val customerRepo: CustomerRepo
) {

    private val addressTypes = listOf("work", "business")

    fun save(addressRequest: AddressRequest): Response {
        val user: User = userService.getLoggedInUser()?: return serviceResponse(400, "an unknown error occurred")
        val customer = customerRepo.findByUser(user)

        val address = if (addressRequest.id != null) {
            addressRepo.findByIdOrNull(addressRequest.id)?: return serviceResponse(message = "Invalid address id")
        }else Address()

        address.address = addressRequest.address
        address.type = if (addressRequest.type in addressTypes) {
            addressRequest.type
        }else addressTypes[0]

        val location: Location = address.location?: Location()
        location.lng = addressRequest.lng
        location.lat = addressRequest.lat
        locationRepo.save(location)

        address.location = location
        addressRepo.save(address)

        customer.address.add(address)
        customerRepo.save(customer)

        return serviceResponse(message = "Address saved")
    }
}