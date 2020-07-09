package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Payment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepo: CrudRepository<Payment, Long> {
}