package festusyuma.com.glaid.service

import festusyuma.com.glaid.dto.GasTypeRequest
import festusyuma.com.glaid.model.GasType
import festusyuma.com.glaid.repository.GasRepo
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.serviceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GasService(
        private val gasRepo: GasRepo
) {

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
}