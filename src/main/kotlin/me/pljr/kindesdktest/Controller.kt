package me.pljr.kindesdktest

import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
class Controller(
    private val service: Service
) {

    @GetMapping("/login")
    fun login(session: HttpSession): RedirectView {
        return service.login(session)
    }

    @GetMapping("/callback")
    fun kindeCallback(
        @RequestParam("code") code: String,
        session: HttpSession
    ): ResponseEntity<String> {
        return try {
            service.callback(code, session)
            ResponseEntity.ok("Authentication successful")
        } catch (e: Exception) {
            ResponseEntity.internalServerError()
                .body("Authentication failed: ${e.message}") 
        }
    }
}