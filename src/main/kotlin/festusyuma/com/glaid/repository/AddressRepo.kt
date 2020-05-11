package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Address
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepo: CrudRepository<Address, Long> {
}