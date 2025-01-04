package co.`in`.acmesense.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FigmaNode(
    val name: String? = null,
    val id: String? = null,
    val children: List<FigmaNode>? = null,
    val document: FigmaNode? = null,
    val nodes: Map<String, FigmaNode>?=null,
    @SerialName("type")
    val type: String?=null
)

