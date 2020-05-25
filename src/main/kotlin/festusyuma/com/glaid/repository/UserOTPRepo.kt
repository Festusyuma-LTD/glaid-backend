package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.UserOTP
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserOTPRepo: CrudRepository<UserOTP, Long>{
    fun findByOtpAndEmailAndExpired(otp: String, email: String, expired:Boolean = false): UserOTP?
    fun findByEmailAndExpired(email: String, expired:Boolean = false): UserOTP?
}