package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.repository.GasRepo
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
        private val customerService: CustomerService,
        private val gasRepo: GasRepo
) {

    @GetMapping
    fun home(): ResponseEntity<Response> {
        val customer = customerService.getLoggedInCustomer()?:
                return response(HttpStatus.BAD_REQUEST, "an error occurred")
        val gasType = gasRepo.findByTypeIn(listOf("diesel", "gas"));
        val data = mapOf<String, Any>(
                "customer" to customer,
                "gasType" to gasType
        )

        return response(message = "Customer Welcome", data = data)
    }

    @GetMapping("validate_token")
    fun validateToken(): ResponseEntity<Response> {
        return if (customerService.getLoggedInCustomer() == null) {
            response(HttpStatus.BAD_REQUEST, "an error occurred")
        }else response()
    }
}