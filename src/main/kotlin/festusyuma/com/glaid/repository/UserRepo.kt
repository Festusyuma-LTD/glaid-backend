package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo: CrudRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByTel(tel: String): User?
}