package festusyuma.com.glaid.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Order (

        @ManyToOne
        var driver: Driver? = null,

        @ManyToOne
        var truck: GasTruck? = null,

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
        var scheduledDate: LocalDateTime = LocalDateTime.now(),

        @ManyToOne
        var status: DeliveryStatus
): Common()