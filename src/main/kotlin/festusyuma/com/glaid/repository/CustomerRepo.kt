package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepo: CrudRepository<Customer, Long> {
}