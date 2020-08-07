package festusyuma.com.glaid.security.controller

import festusyuma.com.glaid.dto.UserOTPRequest
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.repository.RoleRepo
import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.security.UserDetailsService
import festusyuma.com.glaid.security.UserPasswordService
import festusyuma.com.glaid.security.model.AuthenticateRequest
import festusyuma.com.glaid.security.model.PasswordUpdateRequest
import festusyuma.com.glaid.security.utl.JWTUtil
import festusyuma.com.glaid.security.utl.PasswordResetRequest
import festusyuma.com.glaid.service.CustomerService
import festusyuma.com.glaid.service.DriverService
import festusyuma.com.glaid.service.UserOTPService
import festusyuma.com.glaid.util.ERROR_OCCURRED_MSG
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
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
import java.lang.Exception

@RestController
class Authenticate (
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

    @PostMapping("/login")
    fun login(@RequestBody req: AuthenticateRequest): ResponseEntity<Response> {
        try {
            authManager.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
        }catch (e: Exception) {
            return response(HttpStatus.UNAUTHORIZED, "Incorrect email or password")
        }

        val userDetails = userDetailsService.loadUserByUsername(req.email)

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