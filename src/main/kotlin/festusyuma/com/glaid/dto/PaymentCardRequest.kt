package festusyuma.com.glaid.dto

data class PaymentCardRequest(
        val id: Long? = null,
        val cardNo: String,
        val reference: String
) {
}