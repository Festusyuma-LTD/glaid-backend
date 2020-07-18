package festusyuma.com.glaid.util

import com.google.firebase.cloud.FirestoreClient

val db = FirestoreClient.getFirestore()

const val USERS = "Users"
const val USER_LOCATIONS = "User Locations"
const val PENDING_ORDERS = "Pending Orders"