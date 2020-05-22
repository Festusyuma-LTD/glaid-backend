package festusyuma.com.glaid.controller.admin

import festusyuma.com.glaid.dto.TruckRequest
import festusyuma.com.glaid.service.TruckService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("admin/truck")
@RequestMapping("admin/truck")
class TruckController(
        private val truckService: TruckService
) {

    @PostMapping("save")
    fun createTruck(@RequestBody truckRequest: TruckRequest): ResponseEntity<Response> {
        val req = truckService.saveTruck(truckRequest)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }
}