package co.`in`.acmesense.auth

class FigmaAuthenticator(private val accessToken: String) {
    fun getAccessToken(): String {
        return accessToken
    }
}