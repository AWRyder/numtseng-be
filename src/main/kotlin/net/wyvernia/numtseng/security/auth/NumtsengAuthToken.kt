package net.wyvernia.numtseng.security.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class NumtsengAuthToken(
    val username: String,
    val isExpiring: Boolean
) : UsernamePasswordAuthenticationToken(username, username, listOf()){

}
