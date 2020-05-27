package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.AddressRequest
import festusyuma.com.glaid.dto.OrderRequest
import festusyuma.com.glaid.model.Address
import festusyuma.com.glaid.model.Orders
import festusyuma.com.glaid.model.Payment
import festusyuma.com.glaid.repository.*
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
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
        private val addressRepo: AddressRepo,
        private val deliveryStatusRepo: DeliveryStatusRepo,
        private val paymentRepo: PaymentRepo
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

            val order = Orders(
                    customer = customer,
                    gasType = gasType,
                    deliveryAddress = deliveryAddress,
                    quantity = orderRequest.quantity,
                    amount = total,
                    deliveryPrice = delivery,
                    tax = tax,
                    scheduledDate = orderRequest.scheduledDate,
                    status = deliveryStatusRepo.findByIdOrNull(1)?: return serviceResponse(400, "an unknown error occurred")
            )

            setOrderPayment(order, orderRequest)

            return if (order.payment?.status == "success" || order.payment?.type == "on_delivery") {
                orderRepo.save(order)
                serviceResponse(message = "order placed", data = order)
            }else serviceResponse(400, order.payment?.status?: "an unknown error occurred")
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
            "card" -> chargeCard(payment)
            "wallet" -> chargeWallet(payment)
            else -> return
        }
    }

    fun chargeCard(payment: Payment) {
    }

    fun chargeWallet(payment: Payment) {
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