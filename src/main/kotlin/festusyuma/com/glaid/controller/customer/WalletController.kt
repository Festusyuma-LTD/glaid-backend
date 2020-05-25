package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.dto.WalletCreditRequest
import festusyuma.com.glaid.service.WalletService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("customer/wallet")
@RequestMapping("customer/wallet")
class WalletController(
        private val service: WalletService
) {

    @PostMapping("/credit")
    fun creditWallet(@RequestBody walletCreditRequest: WalletCreditRequest): ResponseEntity<Response> {
        val req = service.creditCustomerWallet(walletCreditRequest)

        return if (req.status == 200) {
            response(message = req.message, data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}