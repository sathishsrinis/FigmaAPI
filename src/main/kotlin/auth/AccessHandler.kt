package auth

import cli.CommandLineParams
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.system.exitProcess

class AccessHandler {
    companion object{
        private val logger = LoggerFactory.getLogger("Main")
        private const val AUTH_CACHE_FILE = "figma_auth.cache"
    }


    fun getAccessToken(params: CommandLineParams): String {
        return params.figmaAccessToken ?: loadCachedToken() ?: run {
            logger.error("Access token not provided in arguments and no cached token found.")
            exitProcess(1)
        }
    }


    private fun loadCachedToken(): String? {
        val cacheFile = File(AUTH_CACHE_FILE)
        if (!cacheFile.exists()) {
            return null
        }
        return try {
            Properties().apply {
                FileInputStream(cacheFile).use { load(it) }
            }.getProperty("figma.access.token")
        } catch (e: Exception) {
            logger.error("Error loading cached token: ${e.message}", e)
            null
        }
    }

    fun saveAccessTokenToCache(token: String) {
        try {
            Properties().apply {
                setProperty("figma.access.token", token)
            }.also { props ->
                FileOutputStream(AUTH_CACHE_FILE).use { props.store(it, "Figma Access Token") }
                logger.info("Access token saved to cache.")
            }
        } catch (e: Exception) {
            logger.error("Error saving access token to cache: ${e.message}", e)
        }
    }
}