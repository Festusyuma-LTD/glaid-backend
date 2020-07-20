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
        @OrderBy("id DESC")
        @JsonIgnoreProperties("driver", "customer.orders")
        var orders: MutableList<Orders> = mutableListOf()
): Common()