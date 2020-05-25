package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.dto.UserRequest
import festusyuma.com.glaid.service.CustomerAccountService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("customer/account")
@RequestMapping("customer")
class UserController (
        val accountService: CustomerAccountService
) {

    @PostMapping("register")
    fun register(@RequestBody customerRequest: UserRequest): ResponseEntity<Response> {
        val res = accountService.register(customerRequest)

        return if(res.status == 200) {
            response(message = res.message, data = res.data)
        }else response(HttpStatus.BAD_REQUEST, message = res.message, data = res.data)
    }
}