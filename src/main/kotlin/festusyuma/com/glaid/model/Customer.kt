package festusyuma.com.glaid.model

import javax.persistence.*

@Entity
data class Customer (

        @OneToOne
        val user: User,

        @OneToOne
        val wallet: Wallet,

        @OneToMany
        var address: MutableList<Address> = mutableListOf(),

        @OneToMany
        var paymentCards: MutableList<PaymentCard> = mutableListOf(),

        @OneToMany
        var orders: MutableList<Orders> = mutableListOf()
): Common()