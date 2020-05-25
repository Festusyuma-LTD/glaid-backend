package festusyuma.com.glaid.model

import com.sun.org.apache.xpath.internal.operations.Bool
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
data class User (

        @Email
        @Column(unique = true)
        var email: String,

        var fullName: String = "",
        var password: String = "",
        var tel: String = "",

        @ManyToOne
        var role: Role? = null,

        var active: Boolean = true,
        var credentialsExpired: Boolean = false,
        var enabled: Boolean = true
): Common()