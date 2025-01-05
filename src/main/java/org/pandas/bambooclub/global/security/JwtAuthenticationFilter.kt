package org.pandas.bambooclub.global.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = request.cookies?.find { it.name == "jwt" }?.value

        if (!token.isNullOrEmpty() && jwtUtil.validateToken(token)) {
            val claims = jwtUtil.getAllClaimsFromToken(token)
            val userId = claims["userId", String::class.java]
            val mbti = claims["mbti", String::class.java]

            val principal = UserPrincipal(userId, mbti)
            val authentication =
                UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    principal.authorities,
                )

            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }
}
