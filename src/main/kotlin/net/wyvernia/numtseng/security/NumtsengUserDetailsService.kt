package net.wyvernia.numtseng.security

import net.wyvernia.numtseng.user.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class NumtsengUserDetailsService(
    val userService: UserService
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userService.findUserByLogin(username)
    }
}