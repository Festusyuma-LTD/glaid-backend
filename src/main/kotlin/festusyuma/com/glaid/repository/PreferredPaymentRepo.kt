package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.PreferredPaymentMethod
import org.springframework.data.repository.CrudRepository

interface PreferredPaymentRepo: CrudRepository<PreferredPaymentMethod, Long> {
}