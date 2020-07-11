package festusyuma.com.glaid.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Orders(

        @ManyToOne
        @JsonManagedReference
        var driver: Driver? = null,

        @ManyToOne
        var truck: GasTruck? = null,

        @ManyToOne
        @JsonManagedReference
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