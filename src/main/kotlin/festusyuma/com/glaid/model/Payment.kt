package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Payment (

        @Id
        @GeneratedValue
        val id: Long? = null,

        val amount: Double,
        var type: String = "",
        var reference: String = "",
        var status: String = "",

        @OneToOne
        val paymentCard: PaymentCard? = null
)