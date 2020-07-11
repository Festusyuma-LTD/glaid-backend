package festusyuma.com.glaid.service

import festusyuma.com.glaid.model.Driver
import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DriverService(
        private val userService: UserService,
        private val driverRepo: DriverRepo
) {

    fun getLoggedInDriver(): Driver? {
        val user = userService.getLoggedInUser()?: return null
        return driverRepo.findByUser(user)
    }

    fun search(query: String): Response {
        val users = userService.searchUser(query)
        return serviceResponse(data = driverRepo.findByUserIn(users))
    }

    fun getByApproved(approved: Boolean): Response {
        return serviceResponse(data = driverRepo.findByApproved(approved))
    }

    fun approveDriver(driverId: Long, approval: Boolean = true): Response {
        val driver: Driver = driverRepo.findByIdOrNull(driverId)?: return serviceResponse(400, message = "Driver not found")
        driver.approved = approval
        driverRepo.save(driver)

        val message = if (approval) "Driver Approved" else "Driver approval revoked"
        return serviceResponse(message = message)
    }
}