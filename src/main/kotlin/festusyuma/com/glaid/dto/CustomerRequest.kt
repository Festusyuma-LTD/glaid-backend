package festusyuma.com.glaid.dto

data class CustomerRequest (

        val fullName: String,
        val email: String,
        val tel: String,
        val password: String,
        val otp: String?
)