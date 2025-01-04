// Copyright (c) 2024 AcmeSense
// Licensed under the MIT License. See LICENSE file in the project root for full license info
package util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import java.io.File
import java.nio.file.Paths

object JsonUtil {
    inline fun <reified T> toJson(data: T): String {
        return Json.encodeToString(data)
    }

    fun saveJsonToFile(fileName: String, data: String, logger: Logger) {
        try {
            val filePath = Paths.get(fileName).toAbsolutePath()
            File(filePath.toString()).writeText(data)
            logger.info("Response saved to file: $filePath")
        } catch (e: Exception) {
            logger.error("Error saving JSON to file: ${e.message}", e)
        }
    }
}