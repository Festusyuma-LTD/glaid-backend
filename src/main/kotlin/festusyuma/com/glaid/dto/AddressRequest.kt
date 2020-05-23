package festusyuma.com.glaid.dto

data class AddressRequest (
        val id: Long? = null,
        val address: String,
        val type: String,
        val lng: String,
        val lat: String
)