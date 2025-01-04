// Copyright (c) 2024 AcmeSense
// Licensed under the MIT License. See LICENSE file in the project root for full license info
import auth.FigmaAuthenticator
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
        val nodeResponse = client.fetchNodes(fileKey, ids)
        logger.info("Response of nodes from client: $nodeResponse")
        return JsonUtil.toJson(nodeResponse)
    }

    suspend fun getFile(fileKey: String): String {
        logger.info("Fetching file for filekey: $fileKey")
        val fileResponse = client.fetchFile(fileKey)
        //logger.info("Response of file from client: $fileResponse")
        return JsonUtil.toJson(fileResponse)
    }
}