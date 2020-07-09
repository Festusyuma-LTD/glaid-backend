package festusyuma.com.glaid.model

import javax.persistence.Entity

@Entity
data class PreferredPaymentMethod (
        var type: String,
        var cardId: Long? = null
): Common()