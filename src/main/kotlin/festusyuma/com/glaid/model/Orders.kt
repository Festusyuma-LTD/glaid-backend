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

        @OneToOne
        @JsonIgnoreProperties("user")
        var driverRating: OrderRating? = null,

        @OneToOne
        @JsonIgnoreProperties("user")
        var customerRating: OrderRating? = null,

        @ManyToOne
        var status: OrderStatus,

        var driverAssignedDate: LocalDateTime? = null,
        var tripStarted: LocalDateTime? = null,
        var tripEnded: LocalDateTime? = null
): Common()