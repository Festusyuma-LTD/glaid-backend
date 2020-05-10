package festusyuma.com.glaid.controller.driver

import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("admin")
class DashboardController {

    @GetMapping("")
    fun home(): ResponseEntity<Response> {
        return response(message = "Admin Welcome")
    }
}