package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.UserOTPRequest
import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.model.UserOTP
import festusyuma.com.glaid.repository.UserOTPRepo
import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.addCountryCode
import festusyuma.com.glaid.util.getOtp
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class UserOTPService(
        private val twilioService: TwilioService,
        private var mailSender: JavaMailSender,
        private val otpRepo: UserOTPRepo,
        private val userRepo: UserRepo
) {

    private fun createOtp(user: User): UserOTP {
        var userOTP = otpRepo.findByEmailAndExpired(user.email)
        if (userOTP != null) {
            userOTP.expired = true
            otpRepo.save(userOTP)
        }

        userOTP = UserOTP(getOtp(), user.email)
        return otpRepo.save(userOTP)
    }

    fun validateOtp(userOTPRequest: UserOTPRequest): Response {
        val errorMessage = "Invalid Token"
        val user = when {
            userOTPRequest.email != null -> userRepo.findByEmail(userOTPRequest.email)
                    ?:return serviceResponse(400, message = errorMessage)
            userOTPRequest.tel != null -> userRepo.findByTel(userOTPRequest.tel)
                    ?:return serviceResponse(400, message = errorMessage)
            else -> return serviceResponse(400, message = errorMessage)
        }

        otpRepo.findByOtpAndEmailAndExpired(userOTPRequest.otp, user.email)
                ?:return serviceResponse(400, message = errorMessage)

        return serviceResponse(message = "Valid token")
    }

    fun sendOtpToNumber(user: User) {
        val userOTP = createOtp(user)
        twilioService.sendSMS(userOTP.otp, addCountryCode(user.tel))
    }

    fun sendOtpToMail(user: User) {
        val userOTP = createOtp(user)
        val message = SimpleMailMessage()
        message.setTo(user.email)
        message.setSubject("OTP Verification")
        message.setText(userOTP.otp)

        mailSender.send(message)
    }
}