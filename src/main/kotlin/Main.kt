
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
