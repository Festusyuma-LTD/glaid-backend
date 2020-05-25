package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Customer
import festusyuma.com.glaid.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepo: CrudRepository<Customer, Long> {
    fun findByUser(user: User): Customer
    fun findByUserIn(users: List<User>): List<Customer>
}