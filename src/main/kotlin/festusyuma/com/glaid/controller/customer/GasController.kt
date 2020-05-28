package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.service.GasService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("customer/gas")
@RequestMapping("customer/gas")
class GasController(
        private val service: GasService
) {

    @GetMapping("{type}/list")
    fun listGasType(@PathVariable type: String): ResponseEntity<Response> {
        val req = service.listGasByType(type)

        return if (req.status == 200) {
            val data = mutableMapOf(
                    "gasType" to req.data,
                    "predefinedQuantities" to listOf(50, 100)
            )

            response(data = data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}