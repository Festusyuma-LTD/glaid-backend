package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.OrderRating
import festusyuma.com.glaid.model.User
import org.springframework.data.repository.CrudRepository

interface OrderRatingRepo: CrudRepository<OrderRating, Long> {
    fun findByUser(user: User): List<OrderRating>
}