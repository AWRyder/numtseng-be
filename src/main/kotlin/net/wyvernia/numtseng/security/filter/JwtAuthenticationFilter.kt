package net.wyvernia.numtseng.security.filter

import io.jsonwebtoken.*
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.wyvernia.numtseng.config.NumtsengConfiguration
import net.wyvernia.numtseng.exceptions.UnauthorizedException
import net.wyvernia.numtseng.security.auth.NumtsengAuthToken
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.io.IOException
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtAuthenticationFilter(
    private val numtsengConfiguration: NumtsengConfiguration,
    private val secretKey: SecretKey,
    authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header: String = request.getHeader("Authorization")
        if (!StringUtils.startsWithIgnoreCase(header, "Bearer")) {
            filterChain.doFilter(request, response)
            return
        }
        try {
            val authentication: NumtsengAuthToken = parseToken(request)
            SecurityContextHolder.getContext().authentication = authentication
            if (authentication.isExpiring) {
                val claimsJws: Jws<Claims> = getClaimsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION))

                val token = Jwts.builder()
                    .claims(claimsJws.payload)
                    .signWith(secretKey)
                    .issuer(numtsengConfiguration.jwt?.issuer)
                    .subject(claimsJws.payload.subject)
                    .expiration(Date(System.currentTimeMillis() + 100000))
                    .compact()
                response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
                response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
            filterChain.doFilter(request, response)
        } catch (e: UnauthorizedException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Token Expired.")
        } catch (e: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
        }
    }

    private fun parseToken(request: HttpServletRequest): NumtsengAuthToken {
        val claimsJws: Jws<Claims> = getClaimsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION))

        val username = claimsJws.payload.subject

        if (!StringUtils.hasText(username)) {
            throw MalformedJwtException("No username.")
        }
        val isExpiring = Date().after(Date(claimsJws.payload.expiration.time - 600))

        return NumtsengAuthToken(username, isExpiring)
    }

    private fun getClaimsFromToken(token: String?): Jws<Claims> {
        if (token != null && token.startsWith("Bearer ")) {
            val claims = token.replace("Bearer ", "")
            return try {
                Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(claims)
            } catch (e: ExpiredJwtException) {
                throw UnauthorizedException("Token expired.")
            } catch (exception: JwtException) {
                throw exception
            }
        }
        throw JwtException("Not a token")
    }


}