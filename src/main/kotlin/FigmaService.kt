// Copyright (c) 2024 AcmeSense
// Licensed under the MIT License. See LICENSE file in the project root for full license info
import auth.FigmaAuthenticator
import client.FigmaApiException
import client.FigmaClient
import org.slf4j.LoggerFactory
import util.JsonUtil

class FigmaService(
    accessToken: String,
    private val authenticator: FigmaAuthenticator = FigmaAuthenticator(accessToken),
    private val client: FigmaClient = FigmaClient(authenticator)
) {
    private val logger = LoggerFactory.getLogger(FigmaService::class.java)


    suspend fun getNodes(fileKey: String, nodeIds: List<String>): String {
        val ids = nodeIds.joinToString(",")
        logger.info("Fetching nodes for file: $fileKey, nodeIds: $nodeIds, joined ids: $ids")
        try {
            val nodeResponse = client.fetchNodes(fileKey, ids)
            logger.info("Response of nodes from client: $nodeResponse")
            return JsonUtil.toJson(nodeResponse)
        } catch (e: FigmaApiException) {
            logger.error("Error fetching nodes: ${e.message}", e)
            throw e
        }
    }

    suspend fun getFile(fileKey: String): String {
        logger.info("Fetching file for filekey: $fileKey")
        try {
            val fileResponse = client.fetchFile(fileKey)
            return JsonUtil.toJson(fileResponse)
        } catch (e: FigmaApiException) {
            logger.error("Error fetching file: ${e.message}", e)
            throw e
        }
    }
}