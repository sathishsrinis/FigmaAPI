import co.`in`.acmesense.FigmaService
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.util.Properties

fun main() {
    val logger = LoggerFactory.getLogger("Main")
    val properties = loadProperties("src/main/resources/application.properties", logger) ?: return

    val figmaAccessToken = properties.getProperty("figma.access.token")
    val fileKey = properties.getProperty("figma.file.key")
    val nodeIds = properties.getProperty("figma.node.ids")
        .split(",")
        .map { it.trim() }

    val figmaService = FigmaService(figmaAccessToken)

    runBlocking {
        fetchAndLogNodes(figmaService, fileKey, nodeIds, logger)
        fetchAndLogFile(figmaService, fileKey, logger)
    }
}

private fun loadProperties(path: String, logger: Logger): Properties? {
    val properties = Properties()
    return try {
        properties.load(FileInputStream(path))
        properties
    } catch (e: Exception) {
        logger.error("Error loading properties file ${e.message}", e)
        null
    }
}

private suspend fun fetchAndLogNodes(figmaService: FigmaService, fileKey: String, nodeIds: List<String>, logger: Logger) {
    try {
        val nodesJson = figmaService.getNodes(fileKey, nodeIds)
        println("Response of Nodes : $nodesJson")
    } catch (e: Exception) {
        logger.error("Error fetching nodes ${e.message}", e)
    }
}


private suspend fun fetchAndLogFile(figmaService: FigmaService, fileKey: String, logger: Logger) {
    try {
        val fileJson = figmaService.getFile(fileKey)
        println("Response of file: $fileJson")
    } catch (e: Exception) {
        logger.error("Error fetching file: ${e.message}", e)
    }
}