package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.dto.PaymentCardRequest
import festusyuma.com.glaid.service.PaymentCardService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("customer/payment")
@RequestMapping("customer/payment")
class PaymentController(
        private val paymentCardService: PaymentCardService
) {

    @GetMapping("card/save/init")
    fun saveCardInit(): ResponseEntity<Response> {
        val req = paymentCardService.saveCardInit()

        return if (req.status == 200) {
            response(data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @PostMapping("card/save")
    fun saveCard(@RequestBody paymentCardRequest: PaymentCardRequest): ResponseEntity<Response> {
        val req = paymentCardService.saveCard(paymentCardRequest)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @GetMapping("cards/list")
    fun listPaymentCards(): ResponseEntity<Response> {
        val req = paymentCardService.getUserPaymentCards()

        return if (req.status == 200) {
            response(message = req.message, data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @GetMapping("card/{cardId}")
    fun getPaymentCard(@PathVariable cardId: Long): ResponseEntity<Response> {
        val req = paymentCardService.getUserPaymentCard(cardId)

        return if (req.status == 200) {
            response(message = req.message, data = req.data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @GetMapping("card/{cardId}/remove")
    fun removeUserPaymentCard(@PathVariable cardId: Long): ResponseEntity<Response> {
        val req = paymentCardService.removeUserPaymentCard(cardId)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}