package festusyuma.com.glaid.model

import java.util.*
import javax.persistence.*

@Entity
data class Booking (

        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne
        val customer: Customer,

        @OneToOne
        val payment: Payment,

        @OneToOne
        val gasType: GasType,

        @OneToOne
        val deliveryAddress: Address,

        val quantity: Int,
        var amount: Double,
        var deliveryPrice: Double,
        var tax: Double,
        var scheduledDate: Date
)