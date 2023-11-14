package net.wyvernia.numtseng.security.auth

import net.wyvernia.numtseng.exceptions.UnauthorizedException
import net.wyvernia.numtseng.user.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) {
    fun validateAuthentication(authenticationDTO: AuthenticationDTO) {
        checkPasswordMatches(authenticationDTO)
        checkUserDisable(authenticationDTO.username)
    }

    private fun checkPasswordMatches(authenticationDTO: AuthenticationDTO) {
        val dbHashedPassword = userService.findHashedPasswordByUserLogin(authenticationDTO.username)
        if (!passwordEncoder.matches(authenticationDTO.password, dbHashedPassword)) {
            throw UnauthorizedException("Bad credentials")
        }
    }
    private fun checkUserDisable(login: String) {
        if ( !userService.findUserIsEnabledByLogin(login) ) {
            throw UnauthorizedException("Bad credentials")
        }
    }
}