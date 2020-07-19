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

//Error messages
const val ERROR_OCCURRED_MSG = "An error occurred"
const val INVALID_ORDER_ID = "Invalid order id"
const val INVALID_DRIVER_ID = "Invalid driver id"
const val DRIVER_HAS_NO_TRUCK = "Driver has not been assigned to any truck yet"
const val DRIVER_BUSY = "Driver is still has pending order"