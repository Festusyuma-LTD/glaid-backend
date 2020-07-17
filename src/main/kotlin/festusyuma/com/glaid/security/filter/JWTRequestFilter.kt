package festusyuma.com.glaid.security.filter

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import festusyuma.com.glaid.security.UserDetails
import festusyuma.com.glaid.security.model.JWTTokenRepo
import festusyuma.com.glaid.security.utl.JWTUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.FileInputStream
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTRequestFilter(
        private val jwtUtil: JWTUtil,
        private val jwtTokenRepo: JWTTokenRepo
): OncePerRequestFilter() {

    private lateinit var fireBaseApp: FirebaseApp

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {

        if (FirebaseApp.getApps().size == 0) {
            val serviceAccount = FileInputStream("C:/Users/festu/Documents/Work/glaid project/backend/glaid/src/main/resources/glaid-tracking-firebase-adminsdk.json")
            val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://glaid-tracking.firebaseio.com/")
                    .build()

            fireBaseApp = FirebaseApp.initializeApp(options)
        }


        val authorization: String? = req.getHeader("Authorization")
        if (authorization?.startsWith("Bearer ") == true) {
            val token = jwtTokenRepo.findByTokenAndExpired(authorization.substring(7))

            if (token != null) {
                val user = token.user

                if (user != null) {
                    if (jwtUtil.validateToken(token, user.email)) {
                        val userDetails = UserDetails(user)
                        val userAuthentication = UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.authorities
                        )

                        userAuthentication.details = WebAuthenticationDetailsSource().buildDetails(req)
                        SecurityContextHolder.getContext().authentication = userAuthentication
                    }
                }
            }
        }

        chain.doFilter(req, res)
    }
}