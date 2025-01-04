package co.`in`.acmesense.client

import co.`in`.acmesense.auth.FigmaAuthenticator
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory


class FigmaClient(private val authenticator: FigmaAuthenticator) {

    private val authenticationHeader = "X-FIGMA-TOKEN"
    private val logger = LoggerFactory.getLogger(FigmaClient::class.java)
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }


}