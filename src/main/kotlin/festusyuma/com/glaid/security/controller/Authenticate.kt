package festusyuma.com.glaid.security.controller

import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.security.UserDetailsService
import festusyuma.com.glaid.security.model.AuthenticateRequest
import festusyuma.com.glaid.security.model.PasswordUpdateRequest
import festusyuma.com.glaid.security.utl.JWTUtil
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@RestController
class Authenticate (
        private val authManager: AuthenticationManager,
        private val userDetailsService: UserDetailsService,
        private val jwtUtil: JWTUtil,
        private val userRepo: UserRepo,
        private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/login")
    fun login(@RequestBody req: AuthenticateRequest): ResponseEntity<Response> {
        try {
            authManager.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
        }catch (e: Exception) {
            return response(HttpStatus.UNAUTHORIZED, "Incorrect email or password")
        }

        val userDetails = userDetailsService.loadUserByUsername(req.email)

        return if (userDetails != null) {
            response(message = "Login successful", data = mapOf("token" to jwtUtil.generateToken(userDetails)))
        }else response(HttpStatus.UNAUTHORIZED, "Incorrect email or password")
    }

    @PostMapping("/change_password")
    fun changePassword(@RequestBody req: PasswordUpdateRequest): ResponseEntity<Response> {

        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepo.findByEmail(email)

        if (user != null) {
            if (passwordEncoder.matches(req.password, user.password)) {
                user.password = passwordEncoder.encode(req.newPassword)
                userRepo.save(user)
                return response(message = "Password changed")
            }
        }

        return response(HttpStatus.BAD_REQUEST, message = "incorrect password")
    }
}