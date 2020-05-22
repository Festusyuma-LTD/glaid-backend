package festusyuma.com.glaid.model

import javax.persistence.Entity

@Entity
data class RegistrationOTP (
        val otp: String,
        val email: String,
        var expired: Boolean = false
): Common()