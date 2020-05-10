package festusyuma.com.glaid.security.filter

import festusyuma.com.glaid.security.UserDetails
import festusyuma.com.glaid.security.model.JWTTokenRepo
import festusyuma.com.glaid.security.utl.JWTUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTRequestFilter(
        private val jwtUtil: JWTUtil,
        private val jwtTokenRepo: JWTTokenRepo
): OncePerRequestFilter() {

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {

        val authorization: String? = req.getHeader("Authorization")
        if (authorization?.startsWith("Bearer ") == true) {
            val token = jwtTokenRepo.findByToken(authorization.substring(7))

            if (token != null) {
                val user = token.user
                val tokenActive = !token.expired

                if (tokenActive && user != null) {
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