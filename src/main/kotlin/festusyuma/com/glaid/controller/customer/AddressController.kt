package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.dto.AddressRequest
import festusyuma.com.glaid.service.AddressService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("customer/address")
@RequestMapping("customer/address")
class AddressController(
        private val service: AddressService
) {

    @PostMapping("save")
    fun saveAddress(@RequestBody addressRequest: AddressRequest): ResponseEntity<Response> {
        val req = service.save(addressRequest)

        return if(req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, message = req.message)
    }
}