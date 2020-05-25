package festusyuma.com.glaid.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import festusyuma.com.glaid.dto.PaystackTransaction
import festusyuma.com.glaid.dto.PaystackTransactionAuthorization
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class PaymentService {

    @Value("\${paystack.secret}")
    private lateinit var paystackSecretKey: String

    fun getReferenceDetails(reference: String): Response {
        val restTemplate = RestTemplate()
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
            val mapper = ObjectMapper()
            var root = mapper.readTree(req.data.toString())

            if (root.path("status").toString() == "true") {
                root = root.path("data")

                val authorization = root.path("authorization")
                val paystackTransaction =  PaystackTransaction(
                        reference,
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

                return serviceResponse(data = paystackTransaction)
            }
        }

        return serviceResponse(400, "Invalid reference")
    }
}