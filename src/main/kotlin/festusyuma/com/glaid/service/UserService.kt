package festusyuma.com.glaid.service

import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
        private val passwordEncoder: PasswordEncoder,
        private val userRepo: UserRepo
) {
    private val errorMessage: String = "An unknown error occurred"

    fun createUser(user: User): Response {
        val existingUser = userRepo.findByEmail(user.email)

        if (existingUser != null) {
            return serviceResponse(400, message = "Email already exist")
        }

        user.password = passwordEncoder.encode(user.password)
        val newUser = userRepo.save(user)

        return if (newUser.id != null) {
            serviceResponse(data = newUser)
        }else serviceResponse(400, errorMessage)
    }

    fun updateUser(user: User): Response {
        return serviceResponse(data = user)
    }
}