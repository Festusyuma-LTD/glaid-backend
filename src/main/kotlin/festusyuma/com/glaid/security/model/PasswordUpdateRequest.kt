package festusyuma.com.glaid.security.model

data class PasswordUpdateRequest (
        val password: String,
        val newPassword: String
)