package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Email

@Entity
data class Referral (

        @Id
        @GeneratedValue
        val id: Long? = null,

        @Email
        val referrer: String,

        @Email
        val referent: String
)