package festusyuma.com.glaid.service

import com.google.firebase.cloud.FirestoreClient
import festusyuma.com.glaid.dto.AddressRequest
import festusyuma.com.glaid.dto.OrderRequest
import festusyuma.com.glaid.dto.PaystackTransaction
import festusyuma.com.glaid.model.Address
import festusyuma.com.glaid.model.Orders
import festusyuma.com.glaid.model.Payment
import festusyuma.com.glaid.model.PaymentCard
import festusyuma.com.glaid.repository.*
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.USER_LOCATIONS
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderService(
        private val customerService: CustomerService,
        private val paymentCardService: PaymentCardService,
        private val paymentService: PaymentService,
        private val addressService: AddressService,
        private val gasRepo: GasRepo,
        private val orderRepo: OrderRepo,
        private val orderStatusRepo: OrderStatusRepo,
        private val paymentRepo: PaymentRepo,
        private val walletRepo: WalletRepo,
        private val customerRepo: CustomerRepo
) {

    fun createOrder(orderRequest: OrderRequest): Response {
        val customer = customerService.getLoggedInCustomer()?:
                return serviceResponse(400, "an unknown error occurred")

        if (orderRequest.paymentType in listOf("card", "wallet", "on_delivery")) {
            if (orderRequest.quantity < 0) return serviceResponse(400, "quantity must be greater than 0")
            val gasType = gasRepo.findByIdOrNull(orderRequest.gasTypeId)?:
                    return serviceResponse(400, "invalid gas id")

            val total = gasType.price * orderRequest.quantity
            val delivery = getDeliveryPrice()
            val tax = getTaxPrice(total)
            val deliveryAddress = getDeliveryAddress(orderRequest.deliveryAddress)
                    ?: return serviceResponse(400, "invalid delivery address")

            if (deliveryAddress.type == "business" && orderRequest.quantity < getMinimumBusinessDelivery()) {
                return serviceResponse(400, "Business orders have to be a minimum of 5000 liters")
            }

            val order = Orders(
                    customer = customer,
                    gasType = gasType,
                    deliveryAddress = deliveryAddress,
                    quantity = orderRequest.quantity,
                    amount = total,
                    deliveryPrice = delivery,
                    tax = tax,
                    scheduledDate = orderRequest.scheduledDate,
                    status = orderStatusRepo.findByIdOrNull(1)?: return serviceResponse(400, "an unknown error occurred")
            )

            setOrderPayment(order, orderRequest)

            if (order.payment != null) {
                return if (order.payment?.status == "success" || order.payment?.type == "on_delivery") {
                    paymentRepo.save(order.payment!!)
                    orderRepo.save(order)
                    customer.orders.add(order)
                    customerRepo.save(customer)

                    val db = FirestoreClient.getFirestore().collection(USER_LOCATIONS)

                    serviceResponse(message = "order placed", data = order)
                }else serviceResponse(400, order.payment?.status?: "an unknown error occurred")
            }
        }

        return serviceResponse(400, "an unknown error occurred")
    }

    fun getDeliveryPrice(): Double {
        //todo implement delivery price
        return 0.0
    }

    fun getTaxPrice(total: Double): Double {
        //todo implement tax price
        return 0.0
    }

    fun getMinimumBusinessDelivery():Double {
        return 5000.0
    }

    fun getDeliveryAddress(addressRequest: AddressRequest): Address? {
        return if (addressRequest.id != null) {
            val req = addressService.getCustomerAddressDetails(addressRequest.id)
            if (req.status == 200) {
                req.data as Address
            }else null
        }else addressService.saveAddress(addressRequest)
    }

    fun setOrderPayment(order: Orders, orderRequest: OrderRequest) {
        val total = order.amount + order.deliveryPrice + order.tax
        var payment = Payment(
                total,
                orderRequest.paymentType,
                status = "pending"
        )

        payment = paymentRepo.save(payment)
        order.payment = payment

        when (payment.type) {
            "card" -> chargeCard(payment, orderRequest)
            "wallet" -> chargeWallet(payment)
            else -> return
        }
    }

    fun chargeCard(payment: Payment, orderRequest: OrderRequest) {

        if (orderRequest.paymentCardId == null) return
        val cardReq = paymentCardService.getUserPaymentCard(orderRequest.paymentCardId)
        val customer = customerService.getLoggedInCustomer()?: return

        if (cardReq.status == 200) {
            val card = cardReq.data as PaymentCard
            val debitReq = paymentService.chargeCard(card.authorizationCode, payment.amount, customer.user)
            if (debitReq.data == null) return

            val transaction = debitReq.data as PaystackTransaction
            if (debitReq.status == 200) {
                payment.status = "success"
            }else payment.status = "failed: ${transaction.gatewayResponse}"
            payment.paymentCard = card

            return
        }

        payment.status = "failed: an unknown error occurred"
    }

    fun chargeWallet(payment: Payment) {
        val customer = customerService.getLoggedInCustomer()?: return
        val wallet = customer.wallet
        var amount = payment.amount

        if (wallet.wallet + wallet.bonus < amount) {
            payment.status = "failed: insufficient funds"
            return
        }

        if (wallet.bonus >= amount) {
            wallet.bonus -= amount
            walletRepo.save(wallet)
            payment.status = "success"

            return
        }

        wallet.bonus = 0.0
        amount -= wallet.bonus
        wallet.wallet -= amount
        payment.status = "success"
        walletRepo.save(wallet)
    }

    fun getCustomerOrders(): Response {
        val customer = customerService.getLoggedInCustomer()
                ?: return serviceResponse(400, "an unknown error occurred")

        return serviceResponse(data = orderRepo.findByCustomer(customer))
    }

    fun getCustomerOrderDetails(orderId: Long): Response {
        val customer = customerService.getLoggedInCustomer()
                ?: return serviceResponse(400, "an unknown error occurred")
        val order = orderRepo.findByIdOrNull(orderId)
                ?: return serviceResponse(400, "invalid order id")

        return if (order.customer == customer) {
            return serviceResponse(data = mapOf(
                    "order" to order,
                    "trackingId" to null
            ))
        }else serviceResponse(400, "invalid order id")
    }
}