package festusyuma.com.glaid.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class GasTruck (

        var make: String,
        var model: String,
        var year: String,
        var color: String,

        @OneToOne
        @JsonIgnoreProperties("orders.truck")
        var driver: Driver? = null
): Common()