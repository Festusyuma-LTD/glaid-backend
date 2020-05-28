package festusyuma.com.glaid.dto

import java.time.LocalDateTime

data class OrderRequest(
        val quantity: Double,
        val gasTypeId: Long,
        val deliveryAddress: AddressRequest,
        val paymentType: String,
        val paymentCardId: Long? = null,
        val scheduledDate: LocalDateTime = LocalDateTime.now()
)