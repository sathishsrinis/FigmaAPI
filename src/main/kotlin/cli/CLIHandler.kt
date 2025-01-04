package cli

import org.slf4j.LoggerFactory

class CLIHandler {
    companion object{
        private val logger = LoggerFactory.getLogger("CLIHandler")
    }

    fun parseCommandLineArgs(args: Array<String>): CommandLineParams? {
        if (args.isEmpty()) {
            logger.error("Usage: <options>\n" +
                    "Options:\n" +
                    "--file-key <fileKey>   : Figma File Key (required)\n" +
                    "--node-ids <nodeIds>   : Figma Node Ids (comma separated) (required if download type is nodes)\n" +
                    "--download-type <type> : Type of download (file or nodes) (required)\n" +
                    "--access-token <token> : Figma Access Token (optional, will use cached if not provided)\n")
            return null
        }

        var fileKey: String? = null
        var nodeIds: String? = null
        var accessToken: String? = null
        var downloadType :String? = null

        var i = 0
        while (i < args.size) {
            when (args[i]) {
                "--file-key" -> {
                    fileKey = args.getOrNull(i + 1)
                    i += 2
                }
                "--node-ids" -> {
                    nodeIds = args.getOrNull(i + 1)
                    i += 2
                }
                "--download-type" -> {
                    downloadType = args.getOrNull(i + 1)
                    i += 2
                }
                "--access-token" -> {
                    accessToken = args.getOrNull(i+1)
                    i +=2
                }
                else -> {
                    logger.error("Unknown option: ${args[i]}")
                    return null
                }
            }
        }

        if (fileKey.isNullOrBlank() ) {
            logger.error("file-key must be provided")
            return null
        }

        if (downloadType.isNullOrBlank()) {
            if (nodeIds.isNullOrEmpty()){
                logger.info("downloadkey and node id not provided selecting 'file'")
                downloadType = "file"
            }else{
                logger.info("downloadkey not provided selecting 'nodes'")
                downloadType = "nodes"
            }
        }

        val nodes = nodeIds?.split(",")?.map { it.trim() } ?: emptyList()

        return CommandLineParams(
            figmaAccessToken = accessToken,
            fileKey = fileKey,
            nodeIds = nodes,
            downloadType = downloadType
        )
    }
}