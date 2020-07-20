package festusyuma.com.glaid.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Orders(

        @ManyToOne
        @JsonIgnoreProperties("orders")
        var driver: Driver? = null,

        @ManyToOne
        @JsonIgnoreProperties("driver")
        var truck: GasTruck? = null,

        @ManyToOne
        @JsonIgnoreProperties("orders")
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
        var scheduledDate: LocalDateTime? = null,

        @ManyToOne
        var status: OrderStatus
): Common()