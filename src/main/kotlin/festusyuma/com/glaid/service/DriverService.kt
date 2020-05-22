package festusyuma.com.glaid.service

import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.stereotype.Service

@Service
class DriverService(
        private val userService: UserService,
        private val driverRepo: DriverRepo
) {

    fun search(query: String): Response {
        val users = userService.searchUser(query)
        return serviceResponse(data = driverRepo.findByUserIn(users))
    }
}