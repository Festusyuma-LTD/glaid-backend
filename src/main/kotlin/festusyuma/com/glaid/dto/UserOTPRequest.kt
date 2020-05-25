package festusyuma.com.glaid.dto

data class UserOTPRequest (
     val otp: String,
     val email: String? = null,
     val tel: String? = null
)