package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepo: CrudRepository<Role, Long> {
}