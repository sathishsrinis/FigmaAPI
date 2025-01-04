// Copyright (c) 2024 AcmeSense
// Licensed under the MIT License. See LICENSE file in the project root for full license info

package cli

data class CommandLineParams(
    val figmaAccessToken: String? = null,
    val fileKey: String,
    val nodeIds: List<String> = emptyList(),
    val downloadType: String
)
