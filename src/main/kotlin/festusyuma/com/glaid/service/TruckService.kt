package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.TruckRequest
import festusyuma.com.glaid.model.Driver
import festusyuma.com.glaid.model.GasTruck
import festusyuma.com.glaid.repository.DriverRepo
import festusyuma.com.glaid.repository.TruckRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TruckService(
        private val truckRepo: TruckRepo,
        private val driverRepo: DriverRepo
) {

    fun saveTruck(truckRequest: TruckRequest): Response {
        val truck = if (truckRequest.id != null) {
            truckRepo.findByIdOrNull(truckRequest.id)?: return serviceResponse(400, message = "invalid truck id")
        }else GasTruck(
                truckRequest.make,
                truckRequest.model,
                truckRequest.year,
                truckRequest.color
        )

        if (truckRequest.id != null) {
            truck.make = truckRequest.make
            truck.model = truckRequest.model
            truck.year = truckRequest.year
            truck.color = truckRequest.color
        }

        truckRepo.save(truck)
        return serviceResponse(message = "Truck saved")
    }

    fun assignDriver(driverId: Long, truckId: Long): Response {
        val driver: Driver = driverRepo.findByIdOrNull(driverId)?: return serviceResponse(400, message = "Driver not found")
        val truck: GasTruck = truckRepo.findByIdOrNull(truckId)?: return serviceResponse(400, message = "Truck not found")

        truck.driver = driver
        truckRepo.save(truck)

        return serviceResponse(message = "Saved")
    }
}