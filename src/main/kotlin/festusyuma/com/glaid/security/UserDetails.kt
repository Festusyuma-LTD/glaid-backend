package festusyuma.com.glaid.security

import festusyuma.com.glaid.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetails (val user: User): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(user.role?.role))
    }

    override fun isEnabled(): Boolean {
        return user.enabled
    }

    override fun getUsername(): String {
        return user.email
    }

    override fun isCredentialsNonExpired(): Boolean {
        return !user.credentialsExpired
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return user.active
    }
}