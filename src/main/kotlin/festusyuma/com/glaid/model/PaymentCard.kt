package festusyuma.com.glaid.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class PaymentCard (

        val carNo: String,
        val expMonth: String,
        val expYear: String,

        @JsonIgnore
        var authorizationCode: String
): Common()