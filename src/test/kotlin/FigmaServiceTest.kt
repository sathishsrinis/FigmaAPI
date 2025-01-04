package co.`in`.acmesense

import co.`in`.acmesense.auth.FigmaAuthenticator
import co.`in`.acmesense.client.FigmaClient
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import co.`in`.acmesense.model.FigmaNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import co.`in`.acmesense.util.JsonUtil

class FigmaServiceTest {

    @Test
    fun `getNodes should return a json string of nodes`(): Unit = runBlocking {
        val mockAuthenticator = mockk<FigmaAuthenticator>()
        val mockClient = mockk<FigmaClient>()

        coEvery { mockAuthenticator.getAccessToken() } returns "test_token"
        coEvery { mockClient.fetchNodes(any(),any()) } returns FigmaNode(nodes=mapOf("1:1" to FigmaNode(name="test")))

        val figmaServiceWithMock = FigmaService(mockAuthenticator.getAccessToken(), mockAuthenticator, mockClient)
        val nodesResult = figmaServiceWithMock.getNodes("test_file", listOf("1:1"))
        assertEquals(nodesResult,JsonUtil.toJson(FigmaNode(nodes=mapOf("1:1" to FigmaNode(name="test")))))
    }

    @Test
    fun `getFile should return json string of figma file`(): Unit = runBlocking{
        val mockAuthenticator = mockk<FigmaAuthenticator>()
        val mockClient = mockk<FigmaClient>()

        coEvery { mockAuthenticator.getAccessToken() } returns "test_token"
        coEvery { mockClient.fetchFile(any()) } returns FigmaNode(name="testFile")

        val figmaServiceWithMock = FigmaService(mockAuthenticator.getAccessToken(), mockAuthenticator, mockClient)
        val fileResult = figmaServiceWithMock.getFile("test_file")
        assertEquals(fileResult,JsonUtil.toJson(FigmaNode(name="testFile")))
    }

}
