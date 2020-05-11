package festusyuma.com.glaid.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun response(status: HttpStatus = HttpStatus.OK, message: String = "success", data: Any? = null): ResponseEntity<Response> {
    return ResponseEntity.ok(Response(status.value(), message, data))
}

fun serviceResponse(status:Boolean = true, data: Any?): Map<String, Any?> {
    return mapOf(
            "status" to status,
            "data" to data
    )
}