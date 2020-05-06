package festusyuma.com.glaid.model

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
        var role: Role? = null
): Common()