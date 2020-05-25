package festusyuma.com.glaid.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Role (
        val role: String
): Common()