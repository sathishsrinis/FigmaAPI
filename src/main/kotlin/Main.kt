import auth.AccessHandler
import cli.CLIHandler
import co.`in`.acmesense.FigmaService
import co.`in`.acmesense.util.JsonUtil
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Main")

fun main(args: Array<String>) {

    val params = CLIHandler().parseCommandLineArgs(args) ?: return

    val accessHandler = AccessHandler()
    val figmaAccessToken = accessHandler.getAccessToken(params)
    if(params.figmaAccessToken != null){
        accessHandler.saveAccessTokenToCache(figmaAccessToken)
    }

    val fileKey = params.fileKey
    val nodeIds = params.nodeIds
    val downloadType = params.downloadType

    val figmaService = FigmaService(figmaAccessToken)

    runBlocking {
        try {
            when (downloadType) {
                "file" -> {
                    val fileJson:String = figmaService.getFile(fileKey)
                    JsonUtil.saveJsonToFile("file_response.json", fileJson, logger)
                }
                "nodes" -> {
                    val nodesJson = figmaService.getNodes(fileKey, nodeIds)
                    JsonUtil.saveJsonToFile("nodes_response.json", nodesJson, logger)
                }
                else -> {
                    logger.error("Invalid download type: $downloadType. Must be 'file' or 'nodes'")
                }
            }
            logger.info("Download completed successfully.")

        } catch (e: Exception) {
            logger.error("Error during download: ${e.message}", e)
        }
    }
}








