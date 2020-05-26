package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.OrderRequest
import festusyuma.com.glaid.repository.GasRepo
import festusyuma.com.glaid.repository.OrderRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderService(
        private val customerService: CustomerService,
        private val paymentService: PaymentService,
        private val gasRepo: GasRepo,
        private val orderRepo: OrderRepo
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


        }

        return serviceResponse(400)
    }

    fun getDeliveryPrice(): Double {
        //todo implement delivery price
        return 0.0
    }

    fun getTaxPrice(total: Double): Double {
        //todo implement tax price
        return 0.0
    }
}