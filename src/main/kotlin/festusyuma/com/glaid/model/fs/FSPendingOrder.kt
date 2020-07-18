package festusyuma.com.glaid.model.fs

import com.google.cloud.firestore.annotation.ServerTimestamp
import java.security.Timestamp

data class FSPendingOrder (
        val user: FSUser,
        val quantity: Double,
        val gasType: String,
        val gasTypeUnit: String,
        val amount: Double,
        val driverId: String? = null,
        val driver: FSUser? = null,

        @ServerTimestamp
        val timestamp: Timestamp? = null
)