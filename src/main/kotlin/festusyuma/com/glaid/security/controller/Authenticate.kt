package festusyuma.com.glaid.security.controller

import festusyuma.com.glaid.security.UserDetailsService
import festusyuma.com.glaid.security.model.AuthenticateRequest
import festusyuma.com.glaid.security.utl.JWTUtil
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@RestController
class Authenticate (
        private val authManager: AuthenticationManager,
        private val userDetailsService: UserDetailsService,
        private val jwtUtil: JWTUtil
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
}