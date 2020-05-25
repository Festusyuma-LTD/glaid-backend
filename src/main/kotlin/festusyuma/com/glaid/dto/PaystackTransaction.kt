package festusyuma.com.glaid.dto

data class PaystackTransaction (
        var reference: String = "",
        var status: String = "",
        var gatewayResponse: String = "",
        var amount: Double = 0.0,
        var authorization: PaystackTransactionAuthorization
)