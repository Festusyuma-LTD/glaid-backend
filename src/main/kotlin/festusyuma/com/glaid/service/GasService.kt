package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.FixedQuantityRequest
import festusyuma.com.glaid.dto.GasTypeRequest
import festusyuma.com.glaid.model.GasType
import festusyuma.com.glaid.model.GasTypeQuantities
import festusyuma.com.glaid.repository.FixedQuantityRepo
import festusyuma.com.glaid.repository.GasRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GasService(
        private val gasRepo: GasRepo,
        private val fixedQuantityRepo: FixedQuantityRepo
) {

    fun list(): MutableList<GasType> {
        return gasRepo.findAll().toMutableList()
    }

    fun save(gasTypeRequest: GasTypeRequest): Response {
        val gasType = if (gasTypeRequest.id != null) {
            gasRepo.findByIdOrNull(gasTypeRequest.id)?: return serviceResponse(400, "Invalid gas id")
        }else {
            GasType()
        }

        gasType.type = gasTypeRequest.type
        gasType.price = gasTypeRequest.price
        gasType.unit = gasTypeRequest.unit
        gasRepo.save(gasType)

        return serviceResponse(message = "Gas Type saved")
    }

    fun listGasByType(type: String): Response {
        val gasType = gasRepo.findByType(type)?:
                return serviceResponse(400, "invalid gas type")

        return serviceResponse(data = gasType)
    }

    fun saveFixedQuantity(fixedQuantityRequest: FixedQuantityRequest): Response {
        val gasType = gasRepo.findByIdOrNull(fixedQuantityRequest.gasTypeId)
                ?: return serviceResponse(400, "Invalid gas id")

        var fixedQuantity: GasTypeQuantities = gasType.fixedQuantities.find {
            it.quantity == fixedQuantityRequest.quantity
        }.also {
            it?.price = fixedQuantityRequest.price
        }?: GasTypeQuantities(
                fixedQuantityRequest.quantity,
                fixedQuantityRequest.price
        )

        if (fixedQuantity.id == null) {
            fixedQuantity = fixedQuantityRepo.save(fixedQuantity)
            gasType.fixedQuantities.add(fixedQuantity)
            gasRepo.save(gasType)
        }else {
            fixedQuantityRepo.save(fixedQuantity)
        }

        return serviceResponse(message = "Fixed quantity saved")
    }

    fun deleteFixedQuantity(id: Long, fixedQuantityId: Long): Response {
        val gasType = gasRepo.findByIdOrNull(id)
                ?: return serviceResponse(400, "Invalid gas id")

        val fixedQuantity = gasType.fixedQuantities.find { it.id == fixedQuantityId }
                ?:return serviceResponse(400, "Invalid quantity id")

        gasType.fixedQuantities.remove(fixedQuantity)
        gasRepo.save(gasType)
        fixedQuantityRepo.delete(fixedQuantity)

        return serviceResponse(message = "Fixed quantity deleted")
    }
}