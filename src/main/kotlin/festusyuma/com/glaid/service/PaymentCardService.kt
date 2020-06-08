package festusyuma.com.glaid.service

import com.fasterxml.jackson.databind.ObjectMapper
import festusyuma.com.glaid.dto.PaymentCardRequest
import festusyuma.com.glaid.dto.PaystackTransaction
import festusyuma.com.glaid.model.Customer
import festusyuma.com.glaid.model.Payment
import festusyuma.com.glaid.model.PaymentCard
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.PaymentCardRepo
import festusyuma.com.glaid.repository.PaymentRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class PaymentCardService(
        private val paymentService: PaymentService,
        private val customerService: CustomerService,
        private val paymentCardRepo: PaymentCardRepo,
        private val customerRepo: CustomerRepo,
        private val paymentRepo: PaymentRepo
) {

    @Value("\${paystack.secret}")
    private lateinit var paystackSecretKey: String

    fun getUserPaymentCards(): Response {
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")
        return serviceResponse(data = customer.paymentCards)
    }

    fun getUserPaymentCard(cardId: Long): Response {
        val card = paymentCardRepo.findByIdOrNull(cardId)?: return serviceResponse(400, "invalid card id")
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")

        return if (card in customer.paymentCards) {
            serviceResponse(data = card)
        }else serviceResponse(400, "invalid card id")
    }

    fun removeUserPaymentCard(cardId: Long): Response {
        val card = paymentCardRepo.findByIdOrNull(cardId)?: return serviceResponse(400, "invalid card id")
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")

        return if (card in customer.paymentCards) {
            customer.paymentCards.remove(card)
            customerRepo.save(customer)

            card.authorizationCode = ""
            paymentCardRepo.save(card)

            return serviceResponse(message = "Card removed")
        }else serviceResponse(400, "invalid card id")
    }

    fun saveCardInit(): Response {
        val customer = customerService.getLoggedInCustomer()
                ?: return serviceResponse(400, "an unknown error occurred")

        val restTemplate = RestTemplate()
        val httpHeaders = HttpHeaders()
        httpHeaders.setBearerAuth(paystackSecretKey)

        val body = mutableMapOf<String, Any>()
        body["amount"] = 5000
        body["email"] = customer.user.email

        val entity = HttpEntity(body, httpHeaders)

        val response = restTemplate.exchange(
                "https://api.paystack.co/transaction/initialize",
                HttpMethod.POST,
                entity,
                String::class.java
        )

        val mapper = ObjectMapper()
        var root = mapper.readTree(response.body.toString())

        if (root.path("status").toString() == "true") {
            root = root.path("data")
            val payment = Payment(
                    5000.0,
                    "card",
                    root.get("reference").asText(),
                    "pending"
            )

            paymentRepo.save(payment)
            return serviceResponse(data = root.get("access_code").asText())
        }

        return serviceResponse(400, "an error occurred")
    }

    fun saveCard(paymentCardRequest: PaymentCardRequest): Response {
        val isReusable = paymentService.authorizationIsReusable(paymentCardRequest.reference)
        val paystackTransactionReq = paymentService.getPaystackTransactionDTO(paymentCardRequest.reference)
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")

        if (isReusable && paystackTransactionReq.status == 200) {
            val paystackTransaction = paystackTransactionReq.data as PaystackTransaction
            val paymentCard = PaymentCard(
                    paystackTransaction.authorization.last4,
                    paystackTransaction.authorization.expMonth,
                    paystackTransaction.authorization.expYear,
                    paystackTransaction.authorization.authorizationCode
            )

            customer.paymentCards.add(paymentCardRepo.save(paymentCard))
            customerRepo.save(customer)

            return serviceResponse(message = "Card saved")
        }

        return serviceResponse(400, "Error adding Card")
    }
}