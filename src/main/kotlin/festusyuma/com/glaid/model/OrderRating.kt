package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity
data class OrderRating(

        @OneToOne
        val order: Order,

        @ManyToOne
        val user: User,

        @ManyToOne
        val driver: Driver,

        var userRating: Int,
        var driverRating: Int
): Common()