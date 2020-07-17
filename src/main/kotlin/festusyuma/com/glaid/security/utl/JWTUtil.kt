package festusyuma.com.glaid.security.utl

import com.google.firebase.auth.FirebaseAuth
import festusyuma.com.glaid.model.Common
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.security.UserDetails
import festusyuma.com.glaid.security.model.JWTToken
import festusyuma.com.glaid.security.model.JWTTokenRepo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*

@Service
class JWTUtil (
        val jwtTokenRepo: JWTTokenRepo,
        val customerRepo: CustomerRepo,
        val driverRepo: DriverRepo
) {
    var body: MutableMap<String, Any>? = null

    fun getUsername(): String? {
        return body?.get("email") as String
    }

    private fun setClaims(token: JWTToken) {
        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token.token)

            val claims: MutableMap<String, Any> = decodedToken.claims
            claims["email"] = decodedToken.uid

            body = claims
        } catch (e: Exception) {
            e.printStackTrace()
            token.expired = true
            jwtTokenRepo.save(token)
        }
    }

    fun generateToken(userDetails: UserDetails): String {
        val gClaims: MutableMap<String, Any> = mutableMapOf(
                "user" to when(userDetails.user.role?.role) {
                    "ADMIN" -> userDetails.user
                    "DRIVER" -> driverRepo.findByUser(userDetails.user)
                    "CUSTOMER" -> customerRepo.findByUser(userDetails.user)
                    else -> ""
                }
        )

        val oldToken: JWTToken? = jwtTokenRepo.findByUserAndExpired(userDetails.user)
        if (oldToken != null) {
            oldToken.expired = true
            jwtTokenRepo.save(oldToken)
        }

        val token = JWTToken(userDetails.user, createToken(userDetails.username, gClaims), false)
        jwtTokenRepo.save(token)

        return token.token
    }

    private fun createToken(email: String, claims: MutableMap<String, Any>): String {
        return FirebaseAuth
                .getInstance()
                .createCustomToken(email, claims)
    }

    fun validateToken(token: JWTToken, email: String): Boolean {
        setClaims(token)

        if (body != null) {
            println("id, ${getUsername()}")
            if (getUsername().equals(email)) {
                return true
            }
        }

        return false
    }
}