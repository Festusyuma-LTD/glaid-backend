package festusyuma.com.glaid.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import festusyuma.com.glaid.dto.PaystackTransaction
import festusyuma.com.glaid.dto.PaystackTransactionAuthorization
import festusyuma.com.glaid.model.User
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.getRequestFactory
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.temporal.TemporalAmount

@Service
class PaymentService {

    @Value("\${PAYSTACK_SECRET}")
    private lateinit var paystackSecretKey: String

    fun getReferenceDetails(reference: String): Response {
        val restTemplate = RestTemplate(getRequestFactory())
        val httpHeaders = HttpHeaders()
        httpHeaders.setBearerAuth(paystackSecretKey)

        val entity = HttpEntity("body", httpHeaders)
        val response = restTemplate.exchange(
                "https://api.paystack.co/transaction/verify/$reference",
                HttpMethod.GET,
                entity,
                String::class.java
        )

        return serviceResponse(response.statusCodeValue, data = response.body)
    }

    fun transactionSuccessful(reference: String): Boolean {

        val req = getPaystackTransactionDTO(reference)

        if (req.status == 200) {
            val paystackTransaction = req.data as PaystackTransaction
            if (paystackTransaction.status == "success") return true
        }

        return false
    }

    fun authorizationIsReusable(reference: String): Boolean {

        if (transactionSuccessful(reference)) {
            val req = getPaystackTransactionDTO(reference)

            if (req.status == 200) {
                val paystackTransaction = req.data as PaystackTransaction
                return paystackTransaction.authorization.reusable
            }
        }

        return false
    }

    fun getPaystackTransactionDTO(reference: String): Response {

        val req = getReferenceDetails(reference)

        if (req.status == 200) {
            val paystackTransaction = convertPaystackRespToDTO(req.data)
            if (paystackTransaction != null) {
                return serviceResponse(data = paystackTransaction)
            }
        }

        return serviceResponse(400, "Invalid reference")
    }

    fun convertPaystackRespToDTO(data: Any?): PaystackTransaction? {
        val mapper = ObjectMapper()
        var root = mapper.readTree(data.toString())

        if (root.path("status").toString() == "true") {
            root = root.path("data")

            val authorization = root.path("authorization")
            return  PaystackTransaction(
                    root.path("reference").asText(),
                    root.path("status").asText(),
                    root.path("gateway_response").asText(),
                    root.path("amount").asDouble(),
                    PaystackTransactionAuthorization(
                            authorization.path("authorization_code").asText(),
                            authorization.path("card_type").asText(),
                            authorization.path("last4").asText(),
                            authorization.path("exp_month").asText(),
                            authorization.path("exp_year").asText(),
                            authorization.path("reusable").asBoolean()
                    )
            )
        }

        return null
    }

    fun chargeCard(auth: String, amount: Double, user: User): Response {
        val restTemplate = RestTemplate(getRequestFactory())
        val httpHeaders = HttpHeaders()
        httpHeaders.setBearerAuth(paystackSecretKey)

        val body = mutableMapOf<String, Any>()
        body["amount"] = amount * 100
        body["authorization_code"] = auth
        body["email"] = user.email

        val entity = HttpEntity(body, httpHeaders)

        val response = restTemplate.exchange(
                "https://api.paystack.co/transaction/charge_authorization",
                HttpMethod.POST,
                entity,
                String::class.java
        )

        if (response.statusCodeValue == 200) {
            val paystackTransaction = convertPaystackRespToDTO(response.body)
            if (paystackTransaction != null) {
                return if (paystackTransaction.status == "success") {
                    serviceResponse(data = paystackTransaction)
                }else serviceResponse(400, paystackTransaction.gatewayResponse)
            }
        }

        return serviceResponse(400, "An unknown error occurred")
    }
}