package festusyuma.com.glaid.security.utl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import festusyuma.com.glaid.model.Common
import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.model.fs.FSUser
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.repository.UserRepo
import festusyuma.com.glaid.security.UserDetails
import festusyuma.com.glaid.security.model.JWTToken
import festusyuma.com.glaid.security.model.JWTTokenRepo
import festusyuma.com.glaid.util.USERS
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
        val driverRepo: DriverRepo,
        val userRepo: UserRepo
) {

    var body: MutableMap<String, Any>? = null
    var userDetails: UserDetails? = null

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

        val token = JWTToken(userDetails.user, createToken(userDetails.user, gClaims), false)
        jwtTokenRepo.save(token)

        return token.token
    }

    private fun createToken(user: User, claims: MutableMap<String, Any>): String {
        val uid = user.id.toString()
        val token = FirebaseAuth.getInstance().createCustomToken(uid, claims)

        //For testing purposes todo to be removed
        val db = FirestoreClient.getFirestore().collection(USERS).document(uid)
        val fsUser = FSUser(user.fullName, user.email, user.tel)
        db.set(fsUser)
        //end

        return token
    }

    fun validateToken(clientToken: String): Boolean {
        setUserDetails(clientToken)

        return if (userDetails != null) {
            true
        }else return false
    }

    fun setUserDetails(clientToken: String){
        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(clientToken)
            val user = userRepo.findByEmail(decodedToken.uid)

            if (user != null) {
                userDetails = UserDetails(user)
            }
        } catch (e: Exception) {
            println("Verify token error: ${e.message}")
        }
    }
}