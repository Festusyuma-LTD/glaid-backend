package festusyuma.com.glaid.controller

import festusyuma.com.glaid.dto.ImageUpload
import festusyuma.com.glaid.service.UserService
import festusyuma.com.glaid.util.Response
import festusyuma.com.glaid.util.response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("user/account")
@RequestMapping("user")
class UserController (
        private val userService: UserService
){

    @PostMapping("upload_image")
    fun uploadImage(@RequestBody imageUpload: ImageUpload): ResponseEntity<Response> {
        val res = userService.uploadImage(imageUpload.imageUrl)

        return if(res.status == 200) {
            response(message = res.message, data = res.data)
        }else response(HttpStatus.BAD_REQUEST, message = res.message, data = res.data)
    }
}