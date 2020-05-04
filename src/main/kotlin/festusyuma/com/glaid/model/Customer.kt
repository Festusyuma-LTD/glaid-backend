package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class Customer (

        @Id
        @GeneratedValue
        val id: Long,

        @OneToOne
        var user: User
) {
}