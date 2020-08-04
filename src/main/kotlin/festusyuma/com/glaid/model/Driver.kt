package festusyuma.com.glaid.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
data class Driver (

        @OneToOne
        val user: User,

        @OneToOne
        val wallet: Wallet,

        var approved: Boolean = false,

        @OneToMany
        @JsonIgnoreProperties("driver", "customer.orders", "truck.driver")
        @OrderBy("driverAssignedDate DESC")
        var orders: MutableList<Orders> = mutableListOf()
): Common()