package me.pljr.kindesdktest

import com.kinde.KindeClient
import com.kinde.KindeClientBuilder
import com.kinde.authorization.AuthorizationType
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(Config::class.java)

    @Bean
    fun kindeClient(): KindeClient {
        logger.info("Initializing KindeClient")
        logger.info("Domain: $domain")
        logger.info("Redirect URI: $redirectUri")
        logger.info("Client ID: $clientId")
        logger.info("Client Secret length: ${clientSecret.length}")
        logger.info("Logout Redirect URI: $logoutRedirectUri")
        
        return KindeClientBuilder.builder()
            .domain(domain)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .redirectUri(redirectUri)
            .grantType(AuthorizationType.CODE)
            .logoutRedirectUri(logoutRedirectUri)
            .scopes("openid profile email offline")
            .build()
            .also { logger.info("KindeClient initialized successfully") }
    }
}