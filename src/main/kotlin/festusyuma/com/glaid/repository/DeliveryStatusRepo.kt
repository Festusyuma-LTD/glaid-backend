package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.DeliveryStatus
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeliveryStatusRepo: CrudRepository<DeliveryStatus, Long> {
}