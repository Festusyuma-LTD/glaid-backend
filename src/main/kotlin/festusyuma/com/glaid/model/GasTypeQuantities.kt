package festusyuma.com.glaid.model

import javax.persistence.Entity

@Entity
data class GasTypeQuantities (
        var quantity: Double,
        var price: Double
): Common()