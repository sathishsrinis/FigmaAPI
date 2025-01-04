// Copyright (c) 2024 AcmeSense
// Licensed under the MIT License. See LICENSE file in the project root for full license info
package client

import auth.FigmaAuthenticator
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import model.FigmaNode
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
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
    }
    private val maxRetries = 3
    private val retryDelay = 1000L //1 seconds


    suspend fun fetchNodes(fileKey: String, ids: String): FigmaNode {
        return executeRequestWithRetries {
            client.get("https://api.figma.com/v1/files/$fileKey/nodes?ids=$ids") {
                headers {
                    append(authenticationHeader, authenticator.getAccessToken())
                }
            }.body()
        }
    }

    suspend fun fetchFile(fileKey: String): FigmaNode {
        return executeRequestWithRetries {
            client.get("https://api.figma.com/v1/files/$fileKey") {
                headers {
                    append(authenticationHeader, authenticator.getAccessToken())
                }
            }.body()
        }
    }

    private suspend fun <T> executeRequestWithRetries(block: suspend () -> T): T {
        var attempts = 0
        while (true) {
            try {
                return block()
            } catch (e: Exception) {
                attempts++
                when (e) {
                    is ClientRequestException -> handleClientRequestException(e, attempts)
                    is ServerResponseException -> handleServerResponseException(e)
                    else -> handleUnknownException(e)
                }
            }
        }
    }

    private suspend fun handleClientRequestException(e: ClientRequestException, attempts: Int) {
        when (e.response.status) {
            HttpStatusCode.TooManyRequests -> handleTooManyRequests(e, attempts)
            HttpStatusCode.Unauthorized -> handleUnauthorizedRequest(e)
            HttpStatusCode.Forbidden -> handleForbiddenRequest(e)
            HttpStatusCode.NotFound -> handleNotFoundRequest(e)
            else -> handleGenericClientError(e)
        }
    }

    private suspend fun handleTooManyRequests(e: ClientRequestException, attempts: Int) {
        if (attempts > maxRetries) {
            logger.error("Max retries exceeded for rate limit error. Aborting request.", e)
            throw FigmaApiException("Rate limit error after max retries. ${e.message}", e)
        }
        logger.warn("Rate limit error occurred. Retrying in $retryDelay ms, attempt $attempts/$maxRetries")
        delay(retryDelay)
    }

    private fun handleUnauthorizedRequest(e: ClientRequestException) {
        logger.error("Unauthorized error occurred: ${e.message}", e)
        throw FigmaApiException("Unauthorized request. Please check your access token.", e)
    }

    private fun handleForbiddenRequest(e: ClientRequestException) {
        logger.error("Forbidden error occurred: ${e.message}", e)
        throw FigmaApiException("Forbidden access to resource.", e)
    }

    private fun handleNotFoundRequest(e: ClientRequestException) {
        logger.error("Not found error occurred: ${e.message}", e)
        throw FigmaApiException("Resource not found.", e)
    }

    private fun handleGenericClientError(e: ClientRequestException) {
        logger.error(
            "Client error occurred, Status: ${e.response.status}, Message: ${e.message}",
            e
        )
        throw FigmaApiException(
            "Client error occurred. Status: ${e.response.status}. ${e.message}",
            e
        )
    }

    private fun handleServerResponseException(e: ServerResponseException) {
        logger.error("Server error occurred, Status: ${e.response.status}, Message: ${e.message}", e)
        throw FigmaApiException(
            "Server error occurred. Status: ${e.response.status}, Message: ${e.message}",
            e
        )
    }

    private fun handleUnknownException(e: Exception) {
        logger.error("Unknown exception occurred, Message: ${e.message}", e)
        throw FigmaApiException("Unknown exception occurred: ${e.message}", e)
    }

}

class FigmaApiException(message: String, cause: Throwable? = null) : Exception(message, cause)
