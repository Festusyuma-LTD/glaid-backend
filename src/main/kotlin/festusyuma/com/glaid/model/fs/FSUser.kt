package festusyuma.com.glaid.model.fs

data class FSUser (
        val id: String,
        val fullName: String,
        val email: String,
        val tel: String,
        val rating: Double? = null
)