package festusyuma.com.glaid.model

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
data class Driver (

        @OneToOne
        val user: User,

        @OneToOne
        val wallet: Wallet,

        var approved: Boolean = false,

        @OneToMany
        @JsonBackReference
        var orders: List<Orders> = listOf()
): Common()