package festusyuma.com.glaid.controller.admin

import festusyuma.com.glaid.dto.FixedQuantityRequest
import festusyuma.com.glaid.dto.GasTypeRequest
import festusyuma.com.glaid.service.GasService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("admin/gas")
@RequestMapping("admin/gas")
class GasController(
        private val gasService: GasService
) {

    @PostMapping("save")
    fun saveGas(@RequestBody gasTypeRequest: GasTypeRequest): ResponseEntity<Response> {
        val req = gasService.save(gasTypeRequest)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @PostMapping("saveFixedQuantity")
    fun saveFixedQuantity(@RequestBody fixedQuantityRequest: FixedQuantityRequest): ResponseEntity<Response> {
        val req = gasService.saveFixedQuantity(fixedQuantityRequest)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @GetMapping("{id}/delete_quantity/{fixedQuantityId}")
    fun deleteFixedQuantity(@PathVariable id: Long, @PathVariable fixedQuantityId: Long): ResponseEntity<Response> {
        val req = gasService.deleteFixedQuantity(id, fixedQuantityId)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}