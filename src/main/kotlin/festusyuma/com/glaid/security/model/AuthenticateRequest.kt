package festusyuma.com.glaid.security.model

data class AuthenticateRequest (val email: String, val password: String, val role: Long, val token: String?= null, val loginType: String = "email")