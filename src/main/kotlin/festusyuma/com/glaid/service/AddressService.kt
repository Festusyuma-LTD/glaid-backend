package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.AddressRequest
import festusyuma.com.glaid.model.Address
import festusyuma.com.glaid.model.Customer
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
        private val customerService: CustomerService,
        private val locationRepo: LocationRepo,
        private val addressRepo: AddressRepo,
        private val customerRepo: CustomerRepo
) {

    private val addressTypes = listOf("work", "business")

    fun saveCustomerAddress(addressRequest: AddressRequest): Response {
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")
        val address = saveAddress(addressRequest)?: return serviceResponse(400, "invalid address id")

        customer.address.add(address)
        customerRepo.save(customer)

        return serviceResponse(message = "Address saved")
    }

    fun saveAddress(addressRequest: AddressRequest): Address? {

        val address = if (addressRequest.id != null) {
            addressRepo.findByIdOrNull(addressRequest.id)?: return null
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
        return addressRepo.save(address)
    }

    fun listAddress(): Response {
        return serviceResponse(data = addressRepo.findAll())
    }

    fun listCustomerAddresses(): Response {
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")
        return serviceResponse(data = customer.address)
    }

    fun getAddressDetails(addressId: Long): Response {
        val address = addressRepo.findByIdOrNull(addressId)?: return serviceResponse(400, "invalid address id")
        return serviceResponse(data = address)
    }

    fun getCustomerAddressDetails(addressId: Long): Response {
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")
        val address = addressRepo.findByIdOrNull(addressId)?: return serviceResponse(400, "invalid address id")

        return if (address in customer.address) {
            serviceResponse(data = address)
        }else serviceResponse(400, "invalid address id")
    }

    fun removeCustomerAddress(addressId: Long): Response {
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")
        val address = addressRepo.findByIdOrNull(addressId)?: return serviceResponse(400, "invalid address id")

        return if (address in customer.address) {
            customer.address.remove(address)
            customerRepo.save(customer)

            serviceResponse(message = "address removed")
        }else serviceResponse(400, "invalid address id")
    }
}