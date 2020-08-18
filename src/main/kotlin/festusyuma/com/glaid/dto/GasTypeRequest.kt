package festusyuma.com.glaid.dto

data class GasTypeRequest (
        val id: Long? = null,
        val type: String,
        val price: Double,
        val unit: String,
        val hasFixedQuantities: Boolean = false
)