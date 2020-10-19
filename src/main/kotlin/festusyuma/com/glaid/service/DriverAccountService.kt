package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.UserRequest
import festusyuma.com.glaid.model.Customer
import festusyuma.com.glaid.model.Driver
import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.model.Wallet
import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.repository.RoleRepo
import festusyuma.com.glaid.repository.WalletRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DriverAccountService(
        private val userService: UserService,
        private val roleRepo: RoleRepo,
        private val walletRepo: WalletRepo,
        private val driverRepo: DriverRepo
) {

    val errorMessage = "An unknown error occurred"

    fun register(driverRequest: UserRequest, verified: Boolean = false): Response {
        val role = roleRepo.findByIdOrNull(2)
                ?: return serviceResponse(400, message = errorMessage)

        var user = User(
                driverRequest.email,
                driverRequest.fullName,
                driverRequest.password,
                driverRequest.tel,
                role
        )

        val req = userService.createUser(user, driverRequest.otp, verified)
        return if (req.status == 200) {
            if (req.message != "verification") {
                user = req.data as User
                val wallet = walletRepo.save(Wallet())
                val driver = Driver(user, wallet)
                driverRepo.save(driver)

                serviceResponse(message = "Registration successful")
            }else req

        }else req
    }
}