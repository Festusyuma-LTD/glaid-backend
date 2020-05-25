package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.PaymentCardRequest
import festusyuma.com.glaid.dto.PaystackTransaction
import festusyuma.com.glaid.model.Customer
import festusyuma.com.glaid.model.PaymentCard
import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.repository.PaymentCardRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PaymentCardService(
        private val paymentService: PaymentService,
        private val customerService: CustomerService,
        private val paymentCardRepo: PaymentCardRepo,
        private val customerRepo: CustomerRepo
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