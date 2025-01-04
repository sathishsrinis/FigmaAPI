// Copyright (c) 2024 AcmeSense
// Licensed under the MIT License. See LICENSE file in the project root for full license info
package client

import auth.FigmaAuthenticator
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
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
    }


    suspend fun fetchNodes(fileKey: String, ids: String): FigmaNode {
        try {
            logger.info("Fetching nodes for file: $fileKey, with ids: $ids")

            return client.get("https://api.figma.com/v1/files/$fileKey/nodes?ids=$ids") {
                headers {
                    append(authenticationHeader, authenticator.getAccessToken())
                }
            }.body()

        } catch (e: Exception) {
            logger.error("Error fetching nodes: ${e.message}", e)
            throw e
        }
    }

    suspend fun fetchFile(fileKey: String): FigmaNode {
        try {
            logger.info("Fetching file for filekey: $fileKey")
            logger.info("Fetching file for filekey: ${HttpHeaders.Authorization}")
            return client.get("https://api.figma.com/v1/files/$fileKey") {
                headers {
                    append(authenticationHeader, authenticator.getAccessToken())
                }
            }.body()
        } catch (e: Exception) {
            logger.error("Error fetching file: ${e.message}", e)
            throw e
        }
    }

}