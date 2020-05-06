package festusyuma.com.glaid.model

import javax.persistence.*

@Entity
data class Driver (

        @Id
        @GeneratedValue
        val id: Long,

        @OneToOne
        val user: User,

        @OneToOne
        val wallet: Wallet,

        @OneToMany
        val bookings: List<Booking> = listOf()
) {
}