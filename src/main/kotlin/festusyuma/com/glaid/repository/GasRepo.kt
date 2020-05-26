package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.GasType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GasRepo: CrudRepository<GasType, Long> {
    fun findByType(type: String): GasType?
}