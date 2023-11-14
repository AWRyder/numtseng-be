package net.wyvernia.numtseng.security.auth

import net.wyvernia.numtseng.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping("/auth")
class AuthController(
    val userService: UserService
) {

    @RequestMapping("/login", method = [RequestMethod.POST])
    fun login() : ResponseEntity<String> {
        return ResponseEntity.ok("cenas")
    }

}