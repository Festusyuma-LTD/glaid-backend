package festusyuma.com.glaid.controller.driver

import festusyuma.com.glaid.service.OrderService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("driver/dashboard")
@RequestMapping("driver/dashboard")
class BookingController(
        private val orderService: OrderService
) {

    @GetMapping("start_trip")
    fun startTrip(): ResponseEntity<Response> {
        val req = orderService.startTrip()

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @GetMapping("complete_trip")
    fun completeTrip(): ResponseEntity<Response> {
        val req = orderService.completeTrip()

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}