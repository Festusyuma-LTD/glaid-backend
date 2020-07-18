package festusyuma.com.glaid.model.fs

import com.google.cloud.firestore.annotation.ServerTimestamp
import java.security.Timestamp

data class FSPendingOrder (
        val userId: Long,
        val driverId: Long,
        val driverName: String,
        val driveRating: Double = 0.0,
        val quantity: Double,
        val gasType: String,
        val amount: Double,

        @ServerTimestamp
        val timestamp: Timestamp? = null
)