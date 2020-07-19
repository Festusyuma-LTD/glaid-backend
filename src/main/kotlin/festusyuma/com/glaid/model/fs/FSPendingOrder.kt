package festusyuma.com.glaid.model.fs

import com.google.cloud.Timestamp
import com.google.cloud.firestore.annotation.ServerTimestamp
import festusyuma.com.glaid.util.OrderStatusCode

data class FSPendingOrder (
        var user: FSUser?= null,
        var quantity: Double?= null,
        var gasType: String?= null,
        var gasTypeUnit: String?= null,
        var amount: Double?= null,
        var driverId: String? = null,
        var driver: FSUser? = null,
        var status: Long? = OrderStatusCode.PENDING,

        @ServerTimestamp
        val timestamp: Timestamp? = null
)