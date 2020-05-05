package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class GasType (

        @Id
        @GeneratedValue
        val id: Long? = null,

        var type: String = "",
        var price: Double = 0.0
)