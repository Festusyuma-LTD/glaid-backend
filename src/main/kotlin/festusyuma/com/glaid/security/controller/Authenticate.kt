package festusyuma.com.glaid.security.controller

import festusyuma.com.glaid.security.UserDetailsService
import festusyuma.com.glaid.security.model.AuthenticateRequest
import festusyuma.com.glaid.security.utl.JWTUtil
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
    fun login(@RequestBody req: AuthenticateRequest): String {
        try {
            authManager.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
        }catch (e: Exception) {
            return ""
        }

        return ""
    }
}