package festusyuma.com.glaid.security.controller

import com.google.api.client.extensions.appengine.http.UrlFetchTransport
import com.google.api.client.googleapis.apache.GoogleApacheHttpTransport
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.json.jackson2.JacksonFactory
import festusyuma.com.glaid.dto.UserOTPRequest
import festusyuma.com.glaid.dto.UserRequest
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.repository.RoleRepo
import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.security.UserDetails
import festusyuma.com.glaid.security.UserDetailsService
import festusyuma.com.glaid.security.UserPasswordService
import festusyuma.com.glaid.security.model.AuthenticateRequest
import festusyuma.com.glaid.security.model.PasswordUpdateRequest
import festusyuma.com.glaid.security.utl.JWTUtil
import festusyuma.com.glaid.security.utl.PasswordResetRequest
import festusyuma.com.glaid.service.UserOTPService
import festusyuma.com.glaid.util.ERROR_OCCURRED_MSG
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
class Authenticate(
        private val authManager: AuthenticationManager,
        private val userDetailsService: UserDetailsService,
        private val userPasswordService: UserPasswordService,
        private val otpService: UserOTPService,
        private val jwtUtil: JWTUtil,
        private val userRepo: UserRepo,
        private val passwordEncoder: PasswordEncoder,
        private val driverRepo: DriverRepo,
        private val customerRepo: CustomerRepo,
        private val roleRepo: RoleRepo
) {

    @Value("\${CLIENT_ID}")
    lateinit var clientId: String

    @Value("\${CLIENT_SECRET}")
    lateinit var clientSecret: String

    @PostMapping("/login")
    fun login(@RequestBody req: AuthenticateRequest): ResponseEntity<Response> {

        var userDetails: UserDetails? = null

        //todo handle google and facebook login
        println("login type: ${req.loginType}")

        when(req.loginType) {
            "email" -> {
                try {
                    authManager.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
                    userDetails = userDetailsService.loadUserByUsername(req.email)
                } catch (e: Exception) {
                    return response(HttpStatus.UNAUTHORIZED, "Incorrect email or password")
                }
            }
            "google" -> {
                val token = req.token
                println("token: $token")
                if (token != null) {
                    val payload = googleSignIn(token)
                            ?: return response(HttpStatus.UNAUTHORIZED, "Incorrect email or password")

                    println("payload: $payload")
                    userDetails = userDetailsService.loadUserByUsername(payload.email)
                    if (userDetails == null) {
                        createAccountFromGooglePayload(payload, req.role)
                    }
                } else return response(HttpStatus.UNAUTHORIZED, "Incorrect email or password")
            }
            "facebook" -> {
            }
        }


        return if (userDetails != null) {
            val driverRole = roleRepo.findByIdOrNull(2)?: response(HttpStatus.BAD_REQUEST, ERROR_OCCURRED_MSG)
            val customerRole = roleRepo.findByIdOrNull(3)?: response(HttpStatus.BAD_REQUEST, ERROR_OCCURRED_MSG)

            val user = when(userDetails.user.role) {
                driverRole -> driverRepo.findByUser(userDetails.user)
                customerRole -> customerRepo.findByUser(userDetails.user)
                else -> null
            }

            response(
                    message = "Login successful",
                    data = mapOf(
                            "token" to jwtUtil.generateToken(userDetails),
                            "user" to user
                    )
            )
        }else response(HttpStatus.UNAUTHORIZED, "Incorrect email or password")
    }

    private fun googleSignIn(token: String): GoogleIdToken.Payload? {
        val jsonFactory = JacksonFactory()
        val verifier = GoogleIdTokenVerifier.Builder(UrlFetchTransport.getDefaultInstance(), jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build()

        val idToken = verifier.verify(token)
        if (idToken != null) {
            val payload: GoogleIdToken.Payload = idToken.payload

            // Print user identifier
            val userId: String = payload.subject
            println("User ID: $userId")

            // Get profile information from payload
            val email: String = payload.email
            val emailVerified: Boolean = java.lang.Boolean.valueOf(payload.emailVerified)
            val name = payload["name"] as String
            val locale = payload["locale"] as String
            val familyName = payload["family_name"] as String
            val givenName = payload["given_name"] as String

            println("Name: $name, Family name: $familyName")

            return payload
        } else {
            return null;
        }
    }

    private fun createAccountFromGooglePayload(payload: GoogleIdToken.Payload, role: Long) {
        when(role) {

        }
    }

    @PostMapping("/change_password")
    fun changePassword(@RequestBody req: PasswordUpdateRequest): ResponseEntity<Response> {

        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepo.findByEmail(email)

        if (user != null) {
            if (passwordEncoder.matches(req.password, user.password)) {
                userPasswordService.changePassword(user, req.password)
                return response(message = "Password changed")
            }
        }

        return response(HttpStatus.BAD_REQUEST, message = "incorrect password")
    }

    @PostMapping("/reset_password")
    fun resetPassword(@RequestBody passwordResetRequest: PasswordResetRequest): ResponseEntity<Response> {
        val req = userPasswordService.resetPassword(passwordResetRequest)

        if (req.status == 200) {
            return response(message = req.message)
        }

        return response(HttpStatus.BAD_REQUEST, req.message, req.data)
    }

    @PostMapping("/validate_otp")
    fun validateOtp(@RequestBody userOTPRequest: UserOTPRequest): ResponseEntity<Response> {
        val req = otpService.validateOtp(userOTPRequest)
        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}