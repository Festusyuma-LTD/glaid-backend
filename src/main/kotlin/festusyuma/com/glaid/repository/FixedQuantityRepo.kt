package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.GasType
import festusyuma.com.glaid.model.GasTypeQuantities
import org.springframework.data.repository.CrudRepository

interface FixedQuantityRepo: CrudRepository<GasTypeQuantities, Long> {
}