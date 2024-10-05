package me.pljr.kindesdktest

import com.kinde.KindeClient
import com.kinde.KindeClientBuilder
import com.kinde.authorization.AuthorizationType
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config(
    @Value("\${app.kinde.domain}")
    private val domain: String,
    @Value("\${app.kinde.redirect-uri}")
    private val redirectUri: String,
    @Value("\${app.kinde.client-id}")
    private val clientId: String,
    @Value("\${app.kinde.client-secret}")
    private val clientSecret: String,
    @Value("\${app.kinde.logout-redirect-uri}")
    private val logoutRedirectUri: String,
) {

    @Bean
    fun kindeClient(): KindeClient = KindeClientBuilder.builder()
        .domain(domain)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .redirectUri(redirectUri)
        .grantType(AuthorizationType.CODE)
        .logoutRedirectUri(logoutRedirectUri)
        .scopes("openid profile email offline")
        .build()
}