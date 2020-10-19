package festusyuma.com.glaid.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import festusyuma.com.glaid.dto.UserRequest
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class GoogleAccountService(
        private val customerAccountService: CustomerAccountService,
        private val driverAccountService: DriverAccountService
) {

    @Value("\${GLAID_CLIENT_ID}")
    lateinit var glaidClientId: String

    @Value("\${GLAID_DRIVER_ID}")
    lateinit var glaidDriverClientId: String

    @Value("\${CLIENT_SECRET}")
    lateinit var clientSecret: String

    fun googleSignIn(token: String): GoogleIdToken.Payload? {
        val jsonFactory = GsonFactory()
        val transport = NetHttpTransport()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(listOf(glaidClientId, glaidDriverClientId))
                .build()

        val idToken = verifier.verify(token)
        println("idToken ID: $idToken")
        println("verifier: $verifier")

        return if (idToken != null) {
            val payload: GoogleIdToken.Payload = idToken.payload
            val userId: String = payload.subject
            println("User ID: $userId")

            payload
        } else null
    }

    fun createAccountFromGooglePayload(payload: GoogleIdToken.Payload, role: Long): Response {
        val userRequest = UserRequest(payload["name"] as String, payload.email, "", "", null)
        val req = when(role) {
            2L -> driverAccountService.register(userRequest)
            3L -> customerAccountService.register(userRequest)
            else -> null
        }

        return req ?: serviceResponse(400)
    }
}