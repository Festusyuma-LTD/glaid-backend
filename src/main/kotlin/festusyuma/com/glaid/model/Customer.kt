package festusyuma.com.glaid.model

import javax.persistence.*

@Entity
data class Customer (

        @OneToOne
        val user: User,

        @OneToOne
        val wallet: Wallet,

        @OneToOne
        var preferredPaymentMethod: PreferredPaymentMethod? = null,

        @OneToMany
        @OrderBy("id DESC")
        var address: MutableList<Address> = mutableListOf(),

        @OneToMany
        @OrderBy("id DESC")
        var paymentCards: MutableList<PaymentCard> = mutableListOf(),

        @OneToMany
        @OrderBy("id DESC")
        var orders: MutableList<Orders> = mutableListOf()
): Common()