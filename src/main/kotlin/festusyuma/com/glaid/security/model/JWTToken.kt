package festusyuma.com.glaid.security.model

import festusyuma.com.glaid.model.Common
import festusyuma.com.glaid.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
data class JWTToken (

        @ManyToOne
        val user: User? = null,
        val token: String,
        val expired: Boolean

): Common() {
    constructor(): this(token = "", expired = true)
}

@Repository
interface JWTTokenRepository: CrudRepository<JWTToken, Long> {
    fun findByToken(token: String): JWTToken
}