package festusyuma.com.glaid.dto

data class PaystackTransactionAuthorization (
        var authorizationCode: String = "",
        var cardType: String = "",
        var last4: String = "",
        var expMonth: String = "",
        var expYear: String = "",
        var reusable: Boolean = false
)