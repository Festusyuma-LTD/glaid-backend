package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Orders
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepo: CrudRepository<Orders, Long> {
}