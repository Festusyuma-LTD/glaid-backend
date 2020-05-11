package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.BankAccount
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BankAccountRepo: CrudRepository<BankAccount, Long> {
}