package festusyuma.com.glaid.util

import com.google.firebase.cloud.FirestoreClient

val db = FirestoreClient.getFirestore()

const val USERS = "Users"
const val USER_LOCATIONS = "User Locations"
const val PENDING_ORDERS = "Pending Orders"

class PaymentType {
    companion object {
        const val CARD = "card"
        const val WALLET = "wallet"
        const val CASH = "on_delivery"

        fun all(): List<String> {
            return listOf(CARD, WALLET, CASH)
        }
    }
}

class AddressType {
    companion object {
        const val HOME = "home"
        const val BUSINESS = "business"

        fun all(): List<String> {
            return listOf(HOME, BUSINESS)
        }
    }
}

class OrderStatusCode {
    companion object {
        const val PENDING: Long = 1
        const val DRIVER_ASSIGNED: Long = 2
        const val ON_THE_WAY: Long = 3
        const val DELIVERED: Long = 4
        const val PAYMENT_PENDING: Long = 5
        const val Failed: Long = 6

        fun all(): List<Long> {
            return listOf(PENDING, DRIVER_ASSIGNED, ON_THE_WAY, DELIVERED)
        }
    }
}

class PaymentStatus {
    companion object {
        const val PENDING: Long = 1
        const val FAILED: Long = 2
        const val SUCCESS: Long = 3
    }
}

//messages
const val ERROR_OCCURRED_MSG = "An error occurred"
const val INVALID_ORDER_ID = "Invalid order id"
const val INVALID_DRIVER_ID = "Invalid driver id"
const val DRIVER_HAS_NO_TRUCK = "Driver has not been assigned to any truck yet"
const val DRIVER_BUSY = "Driver is still has pending order"
const val DRIVER_ASSIGNED = "A driver has been assigned to this order"
const val NO_PENDING_ORDER = "no pending order"
const val TRIP_STARTED = "trip started"
const val ORDER_COMPLETED = "order completed"
const val CUSTOMER_RATED = "Customer has been rated"
const val DRIVER_RATED = "Driver has been rated"

