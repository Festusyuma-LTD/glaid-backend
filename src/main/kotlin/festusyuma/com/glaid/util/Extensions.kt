package festusyuma.com.glaid.util

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import kotlin.random.Random

fun response(status: HttpStatus = HttpStatus.OK, message: String = "success", data: Any? = null): ResponseEntity<Response> {
    return ResponseEntity.ok(Response(status.value(), message, data))
}

fun serviceResponse(status: Int = 200, message: String = "success", data: Any? = null): Response {
    return Response(status, message, data)
}

fun getOtp(): String {
    val otp = Random.nextInt(100000, 999999)
    return otp.toString()
}

fun addCountryCode(tel: String): String {
    return "+234${tel.substring(1)}"
}

fun getRequestFactory(): HttpComponentsClientHttpRequestFactory {
    val httpClient = HttpClients.custom()
            .setSSLHostnameVerifier(NoopHostnameVerifier())
            .build()
    val requestFactory = HttpComponentsClientHttpRequestFactory()
    requestFactory.httpClient = httpClient

    return requestFactory
}