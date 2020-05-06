package festusyuma.com.glaid.model

import javax.persistence.*

@Entity
data class Customer (

        @OneToOne
        val user: User,

        @OneToOne
        val wallet: Wallet,

        @OneToMany
        var address: List<Address> = listOf(),

        @OneToMany
        var paymentCard: List<PaymentCard> = listOf(),

        @OneToMany
        var bookings: List<Booking> = listOf()
): Common()