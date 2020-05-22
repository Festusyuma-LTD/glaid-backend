package festusyuma.com.glaid.service

import festusyuma.com.glaid.model.RegistrationOTP
import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.repository.RegistrationOTPRepo
import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.util.*
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
        private val passwordEncoder: PasswordEncoder,
        private val twilioService: TwilioService,
        private val userRepo: UserRepo,
        private val otpRepo: RegistrationOTPRepo
) {
    private val errorMessage: String = "An unknown error occurred"

    fun createUser(user: User, otp: String? = null): Response {
        val existingUser = userRepo.findByEmail(user.email)

        if (existingUser != null) {
            return serviceResponse(400, message = "Email already exist")
        }

        if (otp == null) {
            var userOTP = otpRepo.findByEmailAndExpired(user.email)
            if (userOTP != null) userOTP.expired = true

            userOTP = RegistrationOTP(getOtp(), user.email)
            otpRepo.save(userOTP)
            twilioService.sendSMS(userOTP.otp, addCountryCode(user.tel))

            return serviceResponse(message = "verification")
        }else {
            otpRepo.findByOtpAndEmailAndExpired(otp, user.email)
                    ?: return serviceResponse(message = "Invalid OTP")
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