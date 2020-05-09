package festusyuma.com.glaid.security

import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.repository.UserRepo
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsService(private val userRepo: UserRepo): UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails? {
        val user: User? = userRepo.findByEmail(email)

        return if (user != null) {
            UserDetails(user)
        }else user
    }
}