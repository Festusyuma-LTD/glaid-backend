package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.dto.OrderRequest
import festusyuma.com.glaid.service.OrderService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("customer/booking")
@RequestMapping("customer/booking")
class BookingController(
        private val service: OrderService
) {

    @PostMapping("order/new")
    fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<Response> {
        val req = service.createOrder(orderRequest)

        return if (req.status == 200) {
            response(data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}