package festusyuma.com.glaid.model

import java.util.*
import javax.persistence.*

@Entity
data class Booking (

        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne
        var driver: Driver? = null,

        @ManyToOne
        val customer: Customer,

        @OneToOne
        val payment: Payment,

        @OneToOne
        val gasType: GasType,

        @OneToOne
        val shippingAddress: Address,

        val quantity: Int,
        var amount: Double,
        var deliveryPrice: Double,
        var tax: Double,
        var scheduledDate: Date,

        @ManyToOne
        var status: DeliveryStatus
)