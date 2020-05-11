package festusyuma.com.glaid.model

import com.sun.org.apache.xpath.internal.operations.Bool
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
data class User (

        @Email
        @Column(unique = true)
        var email: String,

        var firstName: String = "",
        var lastName: String = "",
        var otherNames: String = "",
        var password: String = "",

        @ManyToOne
        var role: Role,

        var active: Boolean = true,
        var credentialsExpired: Boolean = false,
        var enabled: Boolean = true
): Common()