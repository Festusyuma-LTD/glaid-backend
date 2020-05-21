package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.CustomerRequest
import festusyuma.com.glaid.model.Customer
import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.model.Wallet
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.RoleRepo
import festusyuma.com.glaid.repository.WalletRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class CustomerAccountService(
        private val roleRepo: RoleRepo,
        private val walletRepo: WalletRepo,
        private val customerRepo: CustomerRepo,

        private val userService: UserService
        ) {
    private val errorMessage: String = "An unknown error occurred"

    fun register(customerRequest: CustomerRequest): Response {
        var user = User(customerRequest.email)
        val role = roleRepo.findByIdOrNull(3)
                ?: return serviceResponse(400, message = errorMessage)

        user.fullName = customerRequest.fullName
        user.password = customerRequest.password
        user.role = role

        val createUserResp = userService.createUser(user)
        return if (createUserResp.status == 200) {
            user = createUserResp.data as User
            val wallet = walletRepo.save(Wallet())
            val customer = Customer(user, wallet)
            customerRepo.save(customer)

            serviceResponse()
        }else createUserResp
    }
}