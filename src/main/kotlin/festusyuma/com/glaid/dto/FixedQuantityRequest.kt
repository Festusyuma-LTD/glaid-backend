package festusyuma.com.glaid.dto

data class FixedQuantityRequest (
        val gasTypeId: Long,
        val quantity: Double,
        val price: Double
)