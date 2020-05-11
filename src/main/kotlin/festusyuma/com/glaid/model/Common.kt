package festusyuma.com.glaid.model

import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class Common (

     @Id
     @GeneratedValue
     var id: Long? = null,

     @CreatedDate
     val created: LocalDateTime = LocalDateTime.now(),

     @UpdateTimestamp
     var updated: LocalDateTime = LocalDateTime.now()
)