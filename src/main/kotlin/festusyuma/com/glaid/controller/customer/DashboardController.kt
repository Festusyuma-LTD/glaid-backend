package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("customer")
@RequestMapping("customer")
class DashboardController() {

    @GetMapping
    fun home(): ResponseEntity<Response> {
        return response(message = "Customer Welcome")
    }
}