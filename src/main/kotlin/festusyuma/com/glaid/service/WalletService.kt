package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.PaystackTransaction
import festusyuma.com.glaid.dto.WalletCreditRequest
import festusyuma.com.glaid.repository.PaymentCardRepo
import festusyuma.com.glaid.repository.WalletRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class WalletService(
        private val customerService: CustomerService,
        private val paymentService: PaymentService,
        private val walletRepo: WalletRepo,
        private val paymentCardRepo: PaymentCardRepo
) {

    fun creditCustomerWallet(walletCreditRequest: WalletCreditRequest): Response {
        val customer = customerService.getLoggedInCustomer()?: return serviceResponse(400, "an unknown error occurred")
        val card = paymentCardRepo.findByIdOrNull(walletCreditRequest.cardId)?: return serviceResponse(400, "invalid card id")
        val req = paymentService.chargeCard(card.authorizationCode, walletCreditRequest.amount, customer.user)

        return if (req.status == 200) {
            val transaction = req.data as PaystackTransaction
            customer.wallet.wallet += transaction.amount / 100

            walletRepo.save(customer.wallet)
            serviceResponse(message = "Wallet credited", data = customer.wallet.wallet)
        }else serviceResponse(400, req.message)
    }
}