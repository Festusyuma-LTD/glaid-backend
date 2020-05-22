package festusyuma.com.glaid.controller.admin

import festusyuma.com.glaid.repository.CustomerRepo
import festusyuma.com.glaid.service.CustomerService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("admin/customer")
@RequestMapping("admin/customer")
class CustomerController(
        private val service: CustomerService,
        private val customerRepo: CustomerRepo
) {

    @GetMapping("list")
    fun getDrivers(): ResponseEntity<Response> {
        return response(data = customerRepo.findAll())
    }

    @GetMapping("{customerId}")
    fun getDriver(@PathVariable customerId: Long): ResponseEntity<Response> {
        val customer = customerRepo.findByIdOrNull(customerId)?: return response(HttpStatus.BAD_REQUEST, message = "User not found")
        return response(data = customer)
    }

    @GetMapping("search/{query}")
    fun searchDrivers(@PathVariable query: String): ResponseEntity<Response> {
        return response(data = service.search(query))
    }
}