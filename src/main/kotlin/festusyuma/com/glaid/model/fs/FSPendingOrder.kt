package festusyuma.com.glaid.model.fs

import com.google.cloud.Timestamp
import com.google.cloud.firestore.annotation.ServerTimestamp

data class FSPendingOrder (
        val user: FSUser?= null,
        val quantity: Double?= null,
        val gasType: String?= null,
        val gasTypeUnit: String?= null,
        val amount: Double?= null,
        val driverId: String? = null,
        val driver: FSUser? = null,
        val status: Long? = 1,

        @ServerTimestamp
        val timestamp: Timestamp? = null
)