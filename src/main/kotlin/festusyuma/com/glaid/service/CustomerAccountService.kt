package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.UserRequest
import festusyuma.com.glaid.model.Customer
import festusyuma.com.glaid.model.PreferredPaymentMethod
import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.model.Wallet
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.PreferredPaymentRepo
import festusyuma.com.glaid.repository.RoleRepo
import festusyuma.com.glaid.repository.WalletRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CustomerAccountService(
        private val roleRepo: RoleRepo,
        private val walletRepo: WalletRepo,
        private val customerRepo: CustomerRepo,
        private val preferredPaymentRepo: PreferredPaymentRepo,
        private val userService: UserService
) {
    private val errorMessage: String = "An unknown error occurred"

    fun register(customerRequest: UserRequest, verified: Boolean = false): Response {
        val role = roleRepo.findByIdOrNull(3)
                ?: return serviceResponse(400, message = errorMessage)

        var user = User(
                customerRequest.email,
                customerRequest.fullName,
                customerRequest.password,
                customerRequest.tel,
                role
        )

        val req = userService.createUser(user, customerRequest.otp, verified)
        return if (req.status == 200) {
            if (req.message != "verification") {
                user = req.data as User
                val wallet = walletRepo.save(Wallet())
                val preferredPayment = preferredPaymentRepo.save(PreferredPaymentMethod("wallet"))
                val customer = Customer(user, wallet, preferredPayment)
                customerRepo.save(customer)

                serviceResponse(message = "User registration successful")
            }else req
        }else req
    }
}