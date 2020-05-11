package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Driver
import festusyuma.com.glaid.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverRepo: CrudRepository<Driver, Long> {
    fun findByUser(user: User): Driver
}