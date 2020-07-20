package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.AddressRequest
import festusyuma.com.glaid.dto.OrderRequest
import festusyuma.com.glaid.dto.PaystackTransaction
import festusyuma.com.glaid.model.*
import festusyuma.com.glaid.model.fs.FSPendingOrder
import festusyuma.com.glaid.model.fs.FSTruck
import festusyuma.com.glaid.model.fs.FSUser
import festusyuma.com.glaid.repository.*
import festusyuma.com.glaid.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderService(
        private val customerService: CustomerService,
        private val paymentCardService: PaymentCardService,
        private val paymentService: PaymentService,
        private val addressService: AddressService,
        private val driverService: DriverService,
        private val gasRepo: GasRepo,
        private val orderRepo: OrderRepo,
        private val orderStatusRepo: OrderStatusRepo,
        private val paymentRepo: PaymentRepo,
        private val walletRepo: WalletRepo,
        private val customerRepo: CustomerRepo,
        private val driverRepo: DriverRepo,
        private val truckRepo: TruckRepo
) {

    fun createOrder(orderRequest: OrderRequest): Response {
        val customer = customerService.getLoggedInCustomer()?:
                return serviceResponse(400, "an unknown error occurred")

        if (orderRequest.paymentType in PaymentType.all()) {
            if (orderRequest.quantity <= 0) return serviceResponse(400, "quantity must be greater than 0")
            val gasType = gasRepo.findByIdOrNull(orderRequest.gasTypeId)?:
                    return serviceResponse(400, "invalid gas id")

            val total = gasType.price * orderRequest.quantity
            val delivery = getDeliveryPrice()
            val tax = getTaxPrice(total)
            val deliveryAddress = getDeliveryAddress(orderRequest.deliveryAddress)
                    ?: return serviceResponse(400, "invalid delivery address")

            if (deliveryAddress.type == AddressType.BUSINESS && orderRequest.quantity < getMinimumBusinessDelivery()) {
                return serviceResponse(400, "Business orders have to be a minimum of 5000 liters")
            }

            val scheduledData = if (deliveryAddress.type == AddressType.HOME) null else orderRequest.scheduledDate

            var order = Orders(
                    customer = customer,
                    gasType = gasType,
                    deliveryAddress = deliveryAddress,
                    quantity = orderRequest.quantity,
                    amount = total,
                    deliveryPrice = delivery,
                    tax = tax,
                    scheduledDate = scheduledData,
                    status = orderStatusRepo.findByIdOrNull(1)?: return serviceResponse(400, "an unknown error occurred")
            )

            setOrderPayment(order, orderRequest)

            if (order.payment != null) {
                return if (order.payment?.status == "success" || order.payment?.type == PaymentType.CASH) {
                    paymentRepo.save(order.payment!!)
                    order = orderRepo.save(order)
                    customer.orders.add(order)
                    customerRepo.save(customer)

                    saveFSPendingOrder(order, customer.user)
                    serviceResponse(message = "order placed", data = order)
                }else serviceResponse(400, order.payment?.status?: "an unknown error occurred")
            }
        }

        return serviceResponse(400, "an unknown error occurred")
    }

    private fun saveFSPendingOrder(order: Orders, user: User) {
        val userRef = db.collection(USERS).document(user.id.toString()).get().get()
        val fsUser = userRef.toObject(FSUser::class.java)

        if (fsUser != null) {
            val pendingOrdersRef = db.collection(PENDING_ORDERS).document(order.id.toString())
            val fsPendingOrders = FSPendingOrder(
                    fsUser,
                    order.quantity,
                    order.gasType.type,
                    order.gasType.unit,
                    order.payment?.amount?: 0.0
            )

            pendingOrdersRef.set(fsPendingOrders)
        }
    }

    fun getDeliveryPrice(): Double {
        //todo implement delivery price
        return 0.0
    }

    fun getTaxPrice(total: Double): Double {
        //todo implement tax price
        return 0.0
    }

    fun getMinimumBusinessDelivery():Double {
        return 5000.0
    }

    fun getDeliveryAddress(addressRequest: AddressRequest): Address? {
        return if (addressRequest.id != null) {
            val req = addressService.getCustomerAddressDetails(addressRequest.id)
            if (req.status == 200) {
                req.data as Address
            }else null
        }else addressService.saveAddress(addressRequest)
    }

    fun setOrderPayment(order: Orders, orderRequest: OrderRequest) {
        val total = order.amount + order.deliveryPrice + order.tax
        var payment = Payment(
                total,
                orderRequest.paymentType,
                status = "pending"
        )

        payment = paymentRepo.save(payment)
        order.payment = payment

        when (payment.type) {
            PaymentType.CARD -> chargeCard(payment, orderRequest)
            PaymentType.WALLET -> chargeWallet(payment)
            else -> return
        }
    }

    fun chargeCard(payment: Payment, orderRequest: OrderRequest) {

        if (orderRequest.paymentCardId == null) return
        val cardReq = paymentCardService.getUserPaymentCard(orderRequest.paymentCardId)
        val customer = customerService.getLoggedInCustomer()?: return

        if (cardReq.status == 200) {
            val card = cardReq.data as PaymentCard
            val debitReq = paymentService.chargeCard(card.authorizationCode, payment.amount, customer.user)
            if (debitReq.data == null) return

            val transaction = debitReq.data as PaystackTransaction
            if (debitReq.status == 200) {
                payment.status = "success"
            }else payment.status = "failed: ${transaction.gatewayResponse}"
            payment.paymentCard = card
            payment.reference = transaction.reference

            return
        }

        payment.status = "failed: an unknown error occurred"
    }

    fun chargeWallet(payment: Payment) {
        val customer = customerService.getLoggedInCustomer()?: return
        val wallet = customer.wallet
        var amount = payment.amount

        if (wallet.wallet + wallet.bonus < amount) {
            payment.status = "failed: insufficient funds"
            return
        }

        if (wallet.bonus >= amount) {
            wallet.bonus -= amount
            walletRepo.save(wallet)
            payment.status = "success"

            return
        }

        wallet.bonus = 0.0
        amount -= wallet.bonus
        wallet.wallet -= amount
        payment.status = "success"
        walletRepo.save(wallet)
    }

    fun getCustomerOrders(): Response {
        val customer = customerService.getLoggedInCustomer()
                ?: return serviceResponse(400, "an unknown error occurred")

        return serviceResponse(data = orderRepo.findByCustomer(customer))
    }

    fun getCustomerOrderDetails(orderId: Long): Response {
        val customer = customerService.getLoggedInCustomer()
                ?: return serviceResponse(400, "an unknown error occurred")
        val order = orderRepo.findByIdOrNull(orderId)
                ?: return serviceResponse(400, "invalid order id")

        return if (order.customer == customer) {
            return serviceResponse(data = mapOf(
                    "order" to order,
                    "trackingId" to null
            ))
        }else serviceResponse(400, "invalid order id")
    }

    fun assignDriverToOrder(orderId: Long, driverId: Long): Response {
        var order = orderRepo.findByIdOrNull(orderId)
        var errorMsg = ""

        if (order != null) {
            if (order.driver != null) {
                return serviceResponse(400, DRIVER_ASSIGNED)
            }

            val driver = driverRepo.findByIdOrNull(driverId)

            if (driver != null) {
                val truck = truckRepo.findByDriver(driver)

                if (truck != null) {

                    if (!driverAssignedToOrder(driver)) {

                        order.driver = driver
                        order.truck = truck
                        order.status = orderStatusRepo.findByIdOrNull(OrderStatusCode.DRIVER_ASSIGNED)
                                ?: return serviceResponse(400, message = ERROR_OCCURRED_MSG)

                        order = orderRepo.save(order)
                        driver.orders.add(order)
                        driverRepo.save(driver)

                        setFsPendingOrderDriver(order, driver, truck)

                        return serviceResponse(400, message = "Driver assigned to order")
                    }else errorMsg = DRIVER_BUSY
                }else errorMsg = DRIVER_HAS_NO_TRUCK
            }else errorMsg = INVALID_DRIVER_ID
        }else errorMsg = INVALID_ORDER_ID

        return serviceResponse(400, errorMsg)
    }

    fun driverAssignedToOrder(driver: Driver): Boolean {
        val completed = orderStatusRepo.findByIdOrNull(4)
        if (completed != null) {
            orderRepo.findByDriverAndStatusNot(driver, completed) ?: return false
        }

        return true
    }

    private fun setFsPendingOrderDriver(order: Orders, driver: Driver, truck: GasTruck) {
        val fsDriver = FSUser(driver.user.fullName, driver.user.email, driver.user.tel)
        val fsTruck = FSTruck(truck.make, truck.model, truck.year, truck.color)

        val pendingOrdersRef = db.collection(PENDING_ORDERS).document(order.id.toString())
        val values = mutableMapOf(
                "driver" to fsDriver,
                "truck" to fsTruck,
                "driverId" to driver.user.id,
                "status" to OrderStatusCode.DRIVER_ASSIGNED
        )

        pendingOrdersRef.update(values)
    }

    fun startTrip(): Response {
        val driver = driverService.getLoggedInDriver()
                ?:return serviceResponse(400, ERROR_OCCURRED_MSG)

        val status = orderStatusRepo.findByIdOrNull(2)
                ?:return serviceResponse(400, ERROR_OCCURRED_MSG)

        val order = orderRepo.findByDriverAndStatus(driver, status)
                ?:return serviceResponse(400, NO_PENDING_ORDER)

        order.status = orderStatusRepo.findByIdOrNull(3)
                ?:return serviceResponse(400, ERROR_OCCURRED_MSG)

        orderRepo.save(order)
        setFsPendingOrderTripStarted(order.id)
        return serviceResponse(message = TRIP_STARTED)
    }

    private fun setFsPendingOrderTripStarted(orderId: Long?) {
        if (orderId != null) {
            val pendingOrdersRef = db.collection(PENDING_ORDERS).document(orderId.toString())
            val values: MutableMap<String, Any> = mutableMapOf("status" to 3)
            pendingOrdersRef.update(values)
        }
    }

    private fun completeTrip(): Response {
        val driver = driverService.getLoggedInDriver()
                ?:return serviceResponse(400, ERROR_OCCURRED_MSG)

        val status = orderStatusRepo.findByIdOrNull(3)
                ?:return serviceResponse(400, ERROR_OCCURRED_MSG)

        val order = orderRepo.findByDriverAndStatus(driver, status)
                ?:return serviceResponse(400, NO_PENDING_ORDER)

        order.status = orderStatusRepo.findByIdOrNull(4)
                ?:return serviceResponse(400, ERROR_OCCURRED_MSG)

        orderRepo.save(order)
        setFsPendingOrderTripEnded(order.id)
        return serviceResponse(message = ORDER_COMPLETED)
    }

    private fun setFsPendingOrderTripEnded(orderId: Long?) {
        if (orderId != null) {
            val pendingOrdersRef = db.collection(PENDING_ORDERS).document(orderId.toString())
            val values: MutableMap<String, Any> = mutableMapOf("status" to 4)
            pendingOrdersRef.update(values)
        }
    }
}