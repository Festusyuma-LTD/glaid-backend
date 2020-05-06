package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.validation.constraints.Email

@Entity
data class Referral (

        @Email
        val referrer: String,

        @Email
        val referent: String

): Common()