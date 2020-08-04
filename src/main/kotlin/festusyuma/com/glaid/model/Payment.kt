package festusyuma.com.glaid.model

import festusyuma.com.glaid.util.PaymentStatus
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Payment (

        val amount: Double,
        var type: String = "",
        var reference: String = "",
        var status: Long = PaymentStatus.PENDING,
        var failedMessage: String = "",

        @OneToOne
        var paymentCard: PaymentCard? = null
): Common()