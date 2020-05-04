package festusyuma.com.glaid.model

import javax.persistence.*

@Entity
data class Customer (

        @Id
        @GeneratedValue
        val id: Long,

        @OneToOne
        val user: User,

        @OneToMany
        var address: List<Address> = listOf()
) {
}