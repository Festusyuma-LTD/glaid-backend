package festusyuma.com.glaid.model

import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Common (

     @Id
     @GeneratedValue
     val id: Long? = null,

     @CreatedDate
     val created: Date,

     @UpdateTimestamp
     var updated: Date
)