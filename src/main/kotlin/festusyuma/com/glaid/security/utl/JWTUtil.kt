package festusyuma.com.glaid.security.utl

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

    private val secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    var body: Claims? = null

    fun getUsername(): String? {
        return body?.subject
    }

    fun getExpiration(): Date? {
        return body?.expiration
    }

    fun isExpired(): Boolean {
        return getExpiration()?.before(Date()) ?: true
    }

    private fun setClaims(token: JWTToken) {
        try {
            body = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token.token)
                    .body

        } catch (e: Exception) {
            token.expired = true
            jwtTokenRepo.save(token)
        }
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims = Jwts.claims()
        claims["user"] = when(userDetails.user.role.role) {
            "ADMIN" -> userDetails.user
            "DRIVER" -> driverRepo.findByUser(userDetails.user)
            "CUSTOMER" -> customerRepo.findByUser(userDetails.user)
            else -> null
        }

        val oldToken: JWTToken? = jwtTokenRepo.findByUserAndExpired(userDetails.user)
        if (oldToken != null) {
            oldToken.expired = true
            jwtTokenRepo.save(oldToken)
        }

        val token = JWTToken(userDetails.user, createToken(userDetails.username, claims), false)
        jwtTokenRepo.save(token)

        return token.token
    }

    private fun createToken(email: String, claims: Claims): String {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + (1000 * 24 * 60 * 60)))
                .signWith(secretKey).compact()
    }

    fun validateToken(token: JWTToken, email: String): Boolean {
        setClaims(token)

        if (body != null && !isExpired()) {
            if (getUsername().equals(email)) {
                return true
            }
        }

        return false
    }
}