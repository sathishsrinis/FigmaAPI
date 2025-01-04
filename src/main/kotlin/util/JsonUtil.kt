package co.`in`.acmesense.util

import co.`in`.acmesense.model.FigmaNode
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

object JsonUtil {
    inline fun <reified T> toJson(data: T): String {
        return Json.encodeToString(data)
    }
}