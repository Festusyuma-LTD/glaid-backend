package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Driver (

        @Id
        @GeneratedValue
        val id: Long,

        @OneToOne
        val user: User,

        @OneToOne
        val wallet: Wallet
) {
}