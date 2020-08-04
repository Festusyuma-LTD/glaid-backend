package festusyuma.com.glaid.repository

import festusyuma.com.glaid.model.Customer
import festusyuma.com.glaid.model.Driver
import festusyuma.com.glaid.model.OrderStatus
import festusyuma.com.glaid.model.Orders
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepo: CrudRepository<Orders, Long> {
    fun findByCustomer(customer: Customer): List<Orders>
    fun findByDriverAndStatusNotIn(driver: Driver, status: List<OrderStatus>): Orders?
    fun findByStatus(status: OrderStatus): Orders?
    fun findByDriverAndStatus(driver: Driver, status: OrderStatus): Orders?
}