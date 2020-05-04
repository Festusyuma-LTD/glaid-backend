package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Address (

        @Id
        @GeneratedValue
        val id: Long? = null,

        @OneToOne
        var location: Location? = null,

        var address: String = "",
        var type: String = ""
) {
}