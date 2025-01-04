package cli

data class CommandLineParams(
    val figmaAccessToken: String? = null,
    val fileKey: String,
    val nodeIds: List<String> = emptyList(),
    val downloadType: String
)
