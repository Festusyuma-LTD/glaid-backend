package festusyuma.com.glaid.controller.customer

import festusyuma.com.glaid.model.GasType
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
            val gasType = req.data as GasType
            val predefinedQuantities: List<Double> = if (gasType.hasFixedQuantity) {
                gasType.fixedQuantities.take(2).map { it.quantity }
            }else listOf(50.0, 100.0)

            val data = mutableMapOf(
                    "gasType" to gasType,
                    "predefinedQuantities" to predefinedQuantities
            )

            response(data = data)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}