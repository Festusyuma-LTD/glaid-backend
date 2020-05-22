package festusyuma.com.glaid.security

import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.repository.UserOTPRepo
import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.security.utl.PasswordResetRequest
import festusyuma.com.glaid.service.UserOTPService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserPasswordService(
        private val userRepo: UserRepo,
        private val otpRepo: UserOTPRepo,
        private val otpService: UserOTPService,
        private val passwordEncoder: PasswordEncoder
) {

    fun changePassword(user: User, newPassword: String) {
        user.password = passwordEncoder.encode(newPassword)
        userRepo.save(user)
    }

    fun resetPassword(passwordResetRequest: PasswordResetRequest): Response {
        val user = when {
            passwordResetRequest.email != null -> userRepo.findByEmail(passwordResetRequest.email)
                    ?:return serviceResponse(400, message = "Email not registered")
            passwordResetRequest.tel != null -> userRepo.findByTel(passwordResetRequest.tel)
                    ?:return serviceResponse(400, message = "Phone number not registered")
            else -> return serviceResponse(400, message = "An error occurred")
        }

        if (passwordResetRequest.otp != null) {
            val otp = otpRepo.findByOtpAndEmailAndExpired(passwordResetRequest.otp, user.email)
                    ?: return serviceResponse(401, message = "Invalid OTP")

            otp.expired = true
            otpRepo.save(otp)

            if (passwordResetRequest.newPassword == null) {
                return serviceResponse(400, message = "An error occurred")
            }else changePassword(user, passwordResetRequest.newPassword)

        }else {
            if (passwordResetRequest.resetWith == "tel") {
                otpService.sendOtpToNumber(user)
            }else otpService.sendOtpToMail(user)

            return serviceResponse(message = "Verification")
        }

        return serviceResponse()
    }
}