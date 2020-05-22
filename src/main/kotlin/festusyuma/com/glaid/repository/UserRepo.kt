package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo: CrudRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByTel(tel: String): User?

    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:query% OR u.email LIKE %:query% OR u.tel LIKE %:query%")
    fun search(query: String): List<User>
}