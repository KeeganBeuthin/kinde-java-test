package me.pljr.kindesdktest

import com.kinde.KindeClient
import com.kinde.authorization.AuthorizationUrl
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.servlet.view.RedirectView

@Service
class Service(
    private val kindeClient: KindeClient
) {
    private val logger = LoggerFactory.getLogger(Service::class.java)

    fun login(session: HttpSession): RedirectView {
        try {
            logger.info("Starting login process")
            val authorizationUrl = kindeClient.clientSession().login()
            logger.info("Generated authorization URL: ${authorizationUrl.url}")
            
            session.setAttribute("AuthorizationUrl", authorizationUrl)
            logger.info("Stored AuthorizationUrl in session")
            
            return RedirectView(authorizationUrl.url.toString())
        } catch (e: Exception) {
            logger.error("Error during login process", e)
            throw e
        }
    }

    fun callback(code: String, session: HttpSession) {
        try {
            logger.info("Callback received with code: $code")
            logger.info("Session ID: ${session.id}")
            
            val authorizationUrl = session.getAttribute("AuthorizationUrl") as? AuthorizationUrl
            logger.info("Retrieved AuthorizationUrl from session: ${authorizationUrl != null}")
            
            if (authorizationUrl == null) {
                logger.error("AuthorizationUrl not found in session")
                throw IllegalStateException("Authorization URL not found in session")
            }
            
            logger.info("Authorization URL: ${authorizationUrl.url}")
            
            logger.info("Initializing client session")
            val clientSession = kindeClient.initClientSession(code, authorizationUrl)
            
            try {
                logger.info("Starting token retrieval")
                logger.info("Raw token response: ${clientSession.toString()}")
                val kindeTokens = clientSession.retrieveTokens()
                logger.info("Token retrieval successful")
                
                // Log token details
                kindeTokens.accessToken?.let { token ->
                    logger.info("Access Token present")
                    logger.info("Access Token type: ${token.javaClass.simpleName}")
                }
                
                kindeTokens.idToken?.let { token ->
                    logger.info("ID Token present")
                    logger.info("ID Token type: ${token.javaClass.simpleName}")
                }
                
                kindeTokens.refreshToken?.let { token ->
                    logger.info("Refresh Token present")
                    logger.info("Refresh Token type: ${token.javaClass.simpleName}")
                } ?: logger.info("No refresh token present")
                
                session.setAttribute("kindeTokens", kindeTokens)
                logger.info("Tokens stored in session")
                
            } catch (e: Exception) {
                logger.error("Token retrieval failed", e)
                logger.error("Exception class: ${e.javaClass.name}")
                logger.error("Exception message: ${e.message}")
                logger.error("Cause: ${e.cause?.message}")
                
                // If it's a ParseException, let's get more details
                if (e.cause is java.text.ParseException) {
                    val parseException = e.cause as java.text.ParseException
                    logger.error("Parse error at position: ${parseException.errorOffset}")
                }
                
                throw e
            }
        } catch (e: Exception) {
            logger.error("Callback processing failed", e)
            throw e
        }
    }
}