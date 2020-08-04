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

    private val addressTypes = listOf("home", "business")

    fun saveCustomerAddress(addressRequest: AddressRequest): Response {
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")
        val address = saveAddress(addressRequest)?: return serviceResponse(400, "invalid address id")
        customerRepo.save(customer)

        return serviceResponse(message = "Address saved", data = address)
    }

    fun saveAddress(addressRequest: AddressRequest): Address? {
        val customer = customerService.getLoggedInCustomer()?: return null
        val addressType = if (addressRequest.type in addressTypes) {
            addressRequest.type
        }else addressTypes[0]

        var address = if (addressRequest.id != null) {
            customer.address.find { address -> address.id == addressRequest.id } ?: return null
        }else {
            customer.address.find { address -> address.type == addressType }
        }

        if (address != null) {
            customer.address.remove(address)
            customerRepo.save(customer)
        }else address = Address()
        address = saveAddressDetails(address, addressRequest)

        if (address.id == null) {
            address = addressRepo.save(address)
            customer.address.add(address)
        }else {
            address = addressRepo.save(address)
        }

        return address
    }

    fun saveAddressAnonymous(addressRequest: AddressRequest): Address {
        val address = saveAddressDetails(Address(), addressRequest)
        return addressRepo.save(address)
    }

    fun saveAddressDetails(address: Address, addressRequest: AddressRequest): Address {
        val addressType = if (addressRequest.type in addressTypes) {
            addressRequest.type
        }else addressTypes[0]

        address.address = addressRequest.address
        address.type = addressType

        val location: Location = address.location?: Location()
        location.lng = addressRequest.lng
        location.lat = addressRequest.lat
        locationRepo.save(location)
        address.location = location

        return address
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