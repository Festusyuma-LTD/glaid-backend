package festusyuma.com.glaid.controller.admin

import festusyuma.com.glaid.repository.OrderRepo
import festusyuma.com.glaid.service.OrderService
import festusyuma.com.glaid.util.INVALID_ORDER_ID
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("admin/booking")
@RequestMapping("admin/booking")
class BookingController(
        private val service: OrderService,
        private val orderRepo: OrderRepo
) {

    @GetMapping("{id}")
    fun orderDetails(@PathVariable id: Long): ResponseEntity<Response> {
        val order = orderRepo.findByIdOrNull(id)
                ?: return response(HttpStatus.BAD_REQUEST, INVALID_ORDER_ID)

        return response(data = order)
    }

    @GetMapping("list")
    fun getAll(): ResponseEntity<Response> {
        return response(data = orderRepo.findAll())
    }

    @GetMapping("assign_driver/{orderId}/{driverId}")
    fun assignDriver(@PathVariable orderId: Long, @PathVariable driverId: Long): ResponseEntity<Response> {
        val req = service.assignDriverToOrder(orderId, driverId)

        return if (req.status == 200) {
            response(data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}