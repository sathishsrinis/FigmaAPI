// Copyright (c) 2024 AcmeSense
// Licensed under the MIT License. See LICENSE file in the project root for full license info
package auth

class FigmaAuthenticator(private val accessToken: String) {
    fun getAccessToken(): String {
        return accessToken
    }
}