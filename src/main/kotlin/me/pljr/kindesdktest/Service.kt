package me.pljr.kindesdktest

import com.kinde.KindeClient
import com.kinde.authorization.AuthorizationUrl
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Service
import org.springframework.web.servlet.view.RedirectView

@Service
class Service(
    private val kindeClient: KindeClient
) {

    fun login(session: HttpSession): RedirectView {
        val authorizationUrl = kindeClient.clientSession().login()
        session.setAttribute("AuthorizationUrl", authorizationUrl)
        return RedirectView(authorizationUrl.url.toString())
    }

    fun callback(code: String, session: HttpSession) {
        val authorizationUrl = session.getAttribute("AuthorizationUrl") as AuthorizationUrl
        val clientSession = kindeClient.initClientSession(code, authorizationUrl)
        val kindeTokens = clientSession.retrieveTokens() // Here we fail
    }
}