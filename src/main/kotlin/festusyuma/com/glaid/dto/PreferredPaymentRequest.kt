package festusyuma.com.glaid.dto

data class PreferredPaymentRequest (
    var type: String,
    var cardId: Long?
)