package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.PaymentCard
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentCardRepo: CrudRepository<PaymentCard, Long> {
}