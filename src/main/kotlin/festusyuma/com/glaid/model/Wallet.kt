package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Wallet (

        @Id
        @GeneratedValue
        val id: Long? = null,

        var wallet: Double = 0.0,
        var bonus: Double = 0.0
)