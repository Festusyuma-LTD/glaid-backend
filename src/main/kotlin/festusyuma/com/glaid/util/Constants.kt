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
            return listOf(PaymentType.CARD, PaymentType.WALLET, PaymentType.CASH)
        }
    }
}

class AddressType {
    companion object {
        const val HOME = "home"
        const val BUSINESS = "business"

        fun all(): List<String> {
            return listOf(AddressType.HOME, AddressType.BUSINESS)
        }
    }
}