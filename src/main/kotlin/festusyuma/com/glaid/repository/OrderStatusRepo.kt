package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.OrderStatus
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderStatusRepo: CrudRepository<OrderStatus, Long> {
}