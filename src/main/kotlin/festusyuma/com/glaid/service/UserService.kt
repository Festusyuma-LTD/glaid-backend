package festusyuma.com.glaid.service

import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.repository.UserOTPRepo
import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.util.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
        private val passwordEncoder: PasswordEncoder,
        private val otpService: UserOTPService,
        private val userRepo: UserRepo,
        private val otpRepo: UserOTPRepo
) {
    private val errorMessage: String = "An unknown error occurred"

    fun createUser(user: User, otp: String? = null): Response {
        val existingUserEmail = userRepo.findByEmail(user.email)
        val existingUserPhone = userRepo.findByTel(user.tel)

        if (existingUserEmail != null) return serviceResponse(400, message = "Email already registered")
        if (existingUserPhone != null) return serviceResponse(400, message = "Phone number already registered")

        if (otp == null) {
            otpService.sendOtpToNumber(user)
            return serviceResponse(message = "verification")
        }else {
            otpRepo.findByOtpAndEmailAndExpired(otp, user.email)
                    ?: return serviceResponse(401, message = "Invalid OTP")
        }

        user.password = passwordEncoder.encode(user.password)
        val newUser = userRepo.save(user)

        return if (newUser.id != null) {
            serviceResponse(data = newUser)
        }else serviceResponse(400, errorMessage)
    }

    fun updateUser(user: User): Response {
        return serviceResponse(data = user)
    }
}