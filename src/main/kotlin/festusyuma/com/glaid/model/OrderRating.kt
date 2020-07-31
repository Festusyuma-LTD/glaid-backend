package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity
data class OrderRating(
        @ManyToOne
        val user: User,

        var userRating: Double
): Common()