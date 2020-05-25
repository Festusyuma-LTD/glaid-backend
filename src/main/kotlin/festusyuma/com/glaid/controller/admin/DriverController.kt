package festusyuma.com.glaid.controller.admin

import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.service.DriverService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("admin/driver")
@RequestMapping("admin/driver")
class DriverController(
        private val service: DriverService,
        private val driverRepo: DriverRepo
) {

    @GetMapping("list")
    fun getDrivers(): ResponseEntity<Response> {
        return response(data = driverRepo.findAll())
    }

    @GetMapping("{driverId}")
    fun getDriver(@PathVariable driverId: Long): ResponseEntity<Response> {
        val driver = driverRepo.findByIdOrNull(driverId)?: return response(HttpStatus.BAD_REQUEST, message = "User not found")
        return response(data = driver)
    }

    @GetMapping("search/{query}")
    fun searchDrivers(@PathVariable query: String): ResponseEntity<Response> {
        return response(data = service.search(query))
    }

    @GetMapping("list/approved")
    fun getApprovedDrivers(): ResponseEntity<Response> {
        return response(data = service.getByApproved(true))
    }

    @GetMapping("list/unapproved")
    fun getUnApprovedDrivers(): ResponseEntity<Response> {
        return response(data = service.getByApproved(false))
    }

    @GetMapping("list/online")
    fun getOnlineDrivers(): ResponseEntity<Response> {
        return response()
    }

    @GetMapping("approve/{driverId}")
    fun approveDriver(@PathVariable driverId: Long): ResponseEntity<Response> {
        val req = service.approveDriver(driverId)

        return if(req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, message = req.message)
    }

    @GetMapping("revoke_approval/{driverId}")
    fun revokeDriverApproval(@PathVariable driverId: Long): ResponseEntity<Response> {
        val req = service.approveDriver(driverId, false)

        return if(req.status == 200) {
            response(message = req.message)
        }else response(HttpStatus.BAD_REQUEST, message = req.message)
    }
}