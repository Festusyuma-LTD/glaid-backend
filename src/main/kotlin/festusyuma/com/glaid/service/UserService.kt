package festusyuma.com.glaid.service

import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
        private val passwordEncoder: PasswordEncoder
) {

    fun createUser(user: User): Map<String, Any?> {
        user.password = passwordEncoder.encode(user.password)
        return serviceResponse(true, user)
    }

    fun updateUser(user: User): User {
        return user
    }
}