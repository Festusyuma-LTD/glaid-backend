package festusyuma.com.glaid.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Orders(

        @ManyToOne
        var driver: Driver? = null,

        @ManyToOne
        var truck: GasTruck? = null,

        @ManyToOne
        val customer: Customer,

        @OneToOne
        var payment: Payment? = null,

        @OneToOne
        val gasType: GasType,

        @OneToOne
        val deliveryAddress: Address,

        val quantity: Double,
        var amount: Double,
        var deliveryPrice: Double,
        var tax: Double,
        var scheduledDate: LocalDateTime = LocalDateTime.now(),

        @ManyToOne
        var status: OrderStatus
): Common()