package festusyuma.com.glaid.controller.driver

import festusyuma.com.glaid.dto.RatingRequest
import festusyuma.com.glaid.service.OrderService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("driver/booking")
@RequestMapping("driver/booking")
class BookingController(
        private val orderService: OrderService
) {

    @GetMapping("{id}")
    fun orderDetails(@PathVariable id: Long): ResponseEntity<Response> {
        val req = orderService.getDriverOrderDetails(id)

        return if (req.status == 200) {
            response(message = req.message, data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @PostMapping("rate_customer")
    fun rateDriver(@RequestBody ratingRequest: RatingRequest): ResponseEntity<Response> {
        val req = orderService.rateCustomer(ratingRequest)

        return if (req.status == 200) {
            response(message = req.message, data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

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

    @GetMapping("confirm_payment")
    fun confirmPayment(): ResponseEntity<Response> {
        val req = orderService.confirmPayment(true)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @GetMapping("payment_failed")
    fun paymentFailed(): ResponseEntity<Response> {
        val req = orderService.confirmPayment(false)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}