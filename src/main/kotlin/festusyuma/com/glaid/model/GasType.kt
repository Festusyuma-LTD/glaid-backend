package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
data class GasType (

        var type: String = "",
        var price: Double = 0.0,
        var unit: String = "liters",
        var hasFixedQuantity: Boolean = false,

        @OneToMany
        var fixedQuantities: MutableList<GasTypeQuantities> = mutableListOf()
): Common()