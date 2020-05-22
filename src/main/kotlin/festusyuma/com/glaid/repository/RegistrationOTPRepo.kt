package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.RegistrationOTP
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RegistrationOTPRepo: CrudRepository<RegistrationOTP, Long>{
    fun findByOtpAndEmailAndExpired(otp: String, email: String, expired:Boolean = false): RegistrationOTP?
    fun findByEmailAndExpired(email: String, expired:Boolean = false): RegistrationOTP?
}