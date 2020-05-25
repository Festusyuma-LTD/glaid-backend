package festusyuma.com.glaid.model

import javax.persistence.*

@Entity
data class Driver (

        @OneToOne
        val user: User,

        @OneToOne
        val wallet: Wallet,

        var approved: Boolean = false,

        @OneToMany
        var orders: List<Orders> = listOf()
): Common()