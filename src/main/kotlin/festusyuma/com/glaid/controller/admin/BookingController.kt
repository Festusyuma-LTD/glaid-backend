package festusyuma.com.glaid.controller.admin

import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("admin/booking")
@RequestMapping("admin/booking")
class BookingController(

) {

    @GetMapping("assign_driver/{orderId}/{driverId}")
    fun assignDriver(@PathVariable orderId: Long, @PathVariable driverId: String): ResponseEntity<Response> {
        val req = service.createOrder(orderRequest)

        return if (req.status == 200) {
            response(data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}