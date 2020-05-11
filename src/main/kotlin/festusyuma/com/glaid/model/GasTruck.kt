package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class GasTruck (

        var make: String,
        var model: String,
        var year: String,
        var color: String,

        @OneToOne
        var driver: Driver
): Common()