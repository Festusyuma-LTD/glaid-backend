package festusyuma.com.glaid.controller.admin

import festusyuma.com.glaid.dto.TruckRequest
import festusyuma.com.glaid.repository.TruckRepo
import festusyuma.com.glaid.service.TruckService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("admin/truck")
@RequestMapping("admin/truck")
class TruckController(
        private val service: TruckService,
        private val truckRepo: TruckRepo
) {

    @PostMapping("save")
    fun createTruck(@RequestBody truckRequest: TruckRequest): ResponseEntity<Response> {
        val req = service.saveTruck(truckRequest)

        return if (req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, req.message)
    }

    @GetMapping("{truckId}")
    fun getTruck(@PathVariable truckId: Long): ResponseEntity<Response> {
        val truck = truckRepo.findByIdOrNull(truckId)?: return response(HttpStatus.BAD_REQUEST, "Truck not found")
        return response(data = truck)
    }

    @GetMapping("{truckId}/track")
    fun track(@PathVariable truckId: String): ResponseEntity<Response> {
        //todo track truck
        return response()
    }
}