package com.plotn.cleverNest.auth

import com.plotn.cleverNest.exceptions.JwtErrorException
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object TokenAuthenticationService {

    val EXPIRATIONTIME: Long = 864000000 // 10 days

    internal val SECRET = "dGF0aWMgZmluYWwgU3RyaW5nIFNFQ1JFVCA9ICJUaGlzSXNBU2VjcmV0YXRpby5qc29ud2VidG9r" +
            "ZW4uaW1wbC5EZWZhdWx0Snd0QnVpbGRlci5zaWduV2l0aChEZWZhdWx0Snd0QnVpbGRlci5qYXZhOjEyMyl+W2pqd3QtaW1wbC" +
            "IGFkZEF1dGhlbnRpY2F0aW9uKEh0dHBTZXJ2bGV0UmVzcG9uc2UgcmVzLCBTdHJpbmcgdXNlcm5hbWUpIHsKICAgICAgICBTdHJ" +
            "0wLjEwLjUuamFyOm5hXSI7CgogICAgc3RhdGljIGZpbmFsIFN0cmluZyBUT0tFTl9QUkVGSVggPSAiQmVhcmVyIjsKCiAgICBzd" +
            "uKG5ldyBEYXRlKFN5c3RlbS5jdXJyZW50VGltZU1pbGxpcygpICsgRVhQSVJBVElPTlRJTUUpKQogICAgICAgICAgICAgICAgLnNp" +
            "Z25XaXRoKFNpZ25hdHVyZUFsZ29yaXRobS5IUzUxMiwgU0VDUkVUKS5jb21wYWN0KCk7CiAgICAgICAgcmVzLmFkZEhlYWRlcihIR" +
            "GF0aWMgZmluYWwgU3RyaW5nIEhFQURFUl9TVFJJTkcgPSAiQXV0aG9yaXphdGlvbiI7CgogICAgcHVibGljIHN0YXRpYyB2b2lk" +
            "pbmcgSldUID0gSnd0cy5idWlsZGVyKCkuc2V0U3ViamVjdCh1c2VybmFtZSkKICAgICAgICAgICAgICAgIC5zZXRFeHBpcmF0aW9" +
            "UFERVJfU1RSSU5HLCBUT0tFTl9QUkVGSVggKyAiICIgKyBKV1QpOwogICAgfQoK"

    internal val TOKEN_PREFIX = "Bearer"

    internal val HEADER_STRING = "Authorization"

    internal val upatUnauthHash = HashMap<String, UsernamePasswordAuthenticationToken>()

    fun addAuthentication(res: HttpServletResponse, username: String): String {
        val JWT = Jwts.builder().setSubject(username)
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact()
        res.addHeader(HEADER_STRING, "$TOKEN_PREFIX $JWT")
        return "$TOKEN_PREFIX $JWT"
    }

    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)
        try {
            if (token != null) {
                // parse the token.
                val user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
                        .getSubject()

                return if (user != null) UsernamePasswordAuthenticationToken(user, null, emptyList()) else null
            } else {
                // plotn - if no token specified, claim that there is "unauth" user login ( for deny - remove next line )
                val sName = "unauth_" + request.remoteAddr.replace("\\.".toRegex(), "_")
                var upatUnauth: UsernamePasswordAuthenticationToken? = upatUnauthHash[sName]
                if (upatUnauth == null) {
                    upatUnauth = UsernamePasswordAuthenticationToken(sName, null, emptyList())
                    upatUnauthHash[sName] = upatUnauth
                }
                return upatUnauth//new UsernamePasswordAuthenticationToken("unauth", null, Collections.emptyList());
            }
        } catch (ex: SignatureException) {
            throw JwtErrorException(ex.javaClass.getSimpleName() + " " + ex.message, ex)
        } catch (ex: ExpiredJwtException) {
            throw JwtErrorException(ex.javaClass.getSimpleName() + " " + ex.message, ex)
        } catch (ex: UnsupportedJwtException) {
            throw JwtErrorException(ex.javaClass.getSimpleName() + " " + ex.message, ex)
        } catch (ex: MalformedJwtException) {
            throw JwtErrorException(ex.javaClass.getSimpleName() + " " + ex.message, ex)
        } catch (ex: IllegalArgumentException) {
            throw JwtErrorException(ex.javaClass.simpleName + " " + ex.message, ex)
        }

    }

}