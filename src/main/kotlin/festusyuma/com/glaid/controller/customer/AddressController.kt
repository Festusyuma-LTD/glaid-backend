package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.dto.AddressRequest
import festusyuma.com.glaid.service.AddressService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("customer/address")
@RequestMapping("customer/address")
class AddressController(
        private val service: AddressService
) {

    @PostMapping("save")
    fun saveAddress(@RequestBody addressRequest: AddressRequest): ResponseEntity<Response> {
        val req = service.saveCustomerAddress(addressRequest)

        return if(req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, message = req.message)
    }

    @GetMapping("list")
    fun getAddresses(): ResponseEntity<Response> {
        val req = service.listCustomerAddresses()

        return if(req.status == 200) {
            response(data = req.data)
        }else response(HttpStatus.BAD_REQUEST, message = req.message)
    }

    @GetMapping("{addressId}")
    fun getAddress(@PathVariable addressId: Long): ResponseEntity<Response> {
        val req = service.getCustomerAddressDetails(addressId)

        return if(req.status == 200) {
            response(data = req.data)
        }else response(HttpStatus.BAD_REQUEST, message = req.message)
    }

    @GetMapping("{addressId}/remove")
    fun removeAddress(@PathVariable addressId: Long): ResponseEntity<Response> {
        val req = service.removeCustomerAddress(addressId)

        return if(req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, message = req.message)
    }
}