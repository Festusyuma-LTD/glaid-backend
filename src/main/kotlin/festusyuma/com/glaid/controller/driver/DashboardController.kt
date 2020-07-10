package festusyuma.com.glaid.controller.driver

import festusyuma.com.glaid.service.DriverService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("driver/dashboard")
@RequestMapping("driver/dashboard")
class DashboardController(
        private val driverService: DriverService
) {

    @GetMapping
    fun home(): ResponseEntity<Response> {
        val customer = driverService.getLoggedInDriver()?:
        return response(HttpStatus.BAD_REQUEST, "an error occurred")

        return response(message = "Customer Welcome", data = customer)
    }

    @GetMapping("validate_token")
    fun validateToken(): ResponseEntity<Response> {
        return if (driverService.getLoggedInDriver() == null) {
            response(HttpStatus.BAD_REQUEST, "an error occurred")
        }else response()
    }
}