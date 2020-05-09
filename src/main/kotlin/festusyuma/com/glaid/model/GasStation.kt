package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.OneToOne

@Entity
data class GasStation (

        var name: String,

        @OneToOne
        var address: Address,

        @ManyToMany
        var gasTypes: List<GasType>
): Common()