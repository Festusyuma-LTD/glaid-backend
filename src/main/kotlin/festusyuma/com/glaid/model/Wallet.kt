package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Wallet (

        var wallet: Double = 0.0,
        var bonus: Double = 0.0
): Common()