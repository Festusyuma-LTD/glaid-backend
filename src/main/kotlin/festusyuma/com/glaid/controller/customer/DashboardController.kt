package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.service.CustomerService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("customer/dashboard")
@RequestMapping("customer/dashboard")
class DashboardController(
        private val customerService: CustomerService
) {

    @GetMapping
    fun home(): ResponseEntity<Response> {
        val customer = customerService.getLoggedInCustomer()?:
                return response(HttpStatus.BAD_REQUEST, "an error occurred")

        return response(message = "Customer Welcome", data = customer)
    }
}