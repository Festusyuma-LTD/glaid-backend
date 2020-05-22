package festusyuma.com.glaid.security.utl

data class PasswordResetRequest (
        val email: String? = null,
        val tel: String?= null,
        val otp: String?= null,
        val newPassword: String? = null,
        val resetWith: String = "mail"
)