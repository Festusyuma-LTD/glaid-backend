package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Wallet
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepo: CrudRepository<Wallet, Long> {
}