package net.wyvernia.numtseng.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.wyvernia.numtseng.config.NumtsengConfiguration
import net.wyvernia.numtseng.exceptions.MethodNotAllowedException
import net.wyvernia.numtseng.security.auth.AuthenticationDTO
import net.wyvernia.numtseng.security.auth.AuthenticationService
import net.wyvernia.numtseng.user.User
import net.wyvernia.numtseng.user.UserService
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
    private val numtsengConfiguration: NumtsengConfiguration,
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    init {
        setFilterProcessesUrl("/auth/login")
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val currentRequest = req as HttpServletRequest
        val wrappedRequest = CachingRequestWrapper(currentRequest)
        super.doFilter(wrappedRequest, res, chain)
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        if (request.method != "POST") {
            logger.info("Auth method not supported.")
            throw MethodNotAllowedException(
                "Authentication method not supported: " + request.method
            )
        }
        val username = obtainUsername(request)?.trim() ?: ""
        val password = obtainPassword(request) ?: ""


        authenticationService.validateAuthentication(AuthenticationDTO(username, password))
        val authRequest = UsernamePasswordAuthenticationToken(username, password)

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest)
        return authenticationManager.authenticate(authRequest)
    }

    override fun obtainPassword(request: HttpServletRequest): String? {
        return obtainBodyParameter(request, "password")
    }

    override fun obtainUsername(request: HttpServletRequest): String? {
        return obtainBodyParameter(request, "username")
    }

    private fun obtainBodyParameter(request: HttpServletRequest, parameterName: String): String? {
        return try {
            val bais = ByteArrayInputStream(request.inputStream.readAllBytes())
            val om = ObjectMapper()
            val reader = om.reader()
            val newNode = reader.readTree(bais)
            if (newNode.has(parameterName)) newNode[parameterName].asText() else null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @Throws(IOException::class)
    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        val mapper = ObjectMapper()
        response.writer.write(mapper.writeValueAsString(failed.message))
        response.writer.flush()
    }

    override fun successfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain, auth: Authentication
    ) {
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
        val user = auth.principal as User
        //Here
        val token = Jwts.builder()
            .subject(user.login)
            .signWith(Keys.hmacShaKeyFor((numtsengConfiguration.jwt?.secret?:"" ).toByteArray()))
            .issuer(numtsengConfiguration.jwt?.issuer?:"numtseng")
            .expiration(Date(System.currentTimeMillis() + 80000))
            .compact()
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
        try {
            response.writer
                .append("Bearer ")
                .append(token)
        } catch (e: IOException) {
            // No need to write as this is just a backup.
        }
    }
}