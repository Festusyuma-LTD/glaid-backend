package festusyuma.com.glaid.service

import festusyuma.com.glaid.model.Customer
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.stereotype.Service

@Service
class CustomerService(
        private val userService: UserService,
        private val customerRepo: CustomerRepo
) {

    fun getLoggedInCustomer(): Customer? {
        val user = userService.getLoggedInUser()?: return null
        return customerRepo.findByUser(user)
    }

    fun search(query: String): Response {
        val users = userService.searchUser(query)
        return serviceResponse(data = customerRepo.findByUserIn(users))
    }
}