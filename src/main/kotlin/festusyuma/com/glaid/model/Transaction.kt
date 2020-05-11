package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.validation.constraints.Email

@Entity
data class Transaction (

        val reference: String,
        val type: String,

        @Email
        val email: String,
        val status: String
): Common()