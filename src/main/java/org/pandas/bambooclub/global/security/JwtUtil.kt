package org.pandas.bambooclub.global.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.pandas.bambooclub.domain.user.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun generateToken(user: User): String {
        val claims =
            mapOf(
                "userId" to user.userId,
                "mbti" to user.mbti,
            )

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun getSignKey(): Key { // 반환 타입 변경
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun getAllClaimsFromToken(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .body

    fun validateToken(token: String): Boolean =
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
}
