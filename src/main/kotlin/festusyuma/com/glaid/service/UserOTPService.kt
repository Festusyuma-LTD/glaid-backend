package festusyuma.com.glaid.service

import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.model.UserOTP
import festusyuma.com.glaid.repository.UserOTPRepo
import festusyuma.com.glaid.util.addCountryCode
import festusyuma.com.glaid.util.getOtp
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class UserOTPService(
        private val twilioService: TwilioService,
        private var mailSender: JavaMailSender,
        private val otpRepo: UserOTPRepo
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