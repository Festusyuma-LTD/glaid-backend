package festusyuma.com.glaid.dto

data class TruckRequest (

        val make: String,
        val model: String,
        val year: String,
        val color: String,
        val id: Long? = null
)