package festusyuma.com.glaid.security

import festusyuma.com.glaid.security.filter.JWTRequestFilter
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebSecurity
class SecurityConfiguration (
        val userDetailsService: UserDetailsService,
        val jwtRequestFilter: JWTRequestFilter
): WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)
    }

    override fun configure(http: HttpSecurity?) {

        http?.cors()?.and()
                ?.csrf()?.disable()
                ?.authorizeRequests()
                ?.antMatchers(
                        "/",
                        "/login",
                        "/customer/register",
                        "/driver/register",
                        "/reset_password",
                        "/validate_otp"
                )?.permitAll()
                ?.antMatchers("/admin/**")?.permitAll()
                ?.antMatchers("/driver/**")?.hasRole("DRIVER")
                ?.antMatchers("/customer/**")?.hasRole("CUSTOMER")
                ?.antMatchers("/change_password")?.authenticated()
                ?.and()?.sessionManagement()
                ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()
                ?.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun encodePassword(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
            }
        }
    }
}