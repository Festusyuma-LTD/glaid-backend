package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.GasTruck
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TruckRepo: CrudRepository<GasTruck, Long> {
}