package co.`in`.acmesense

import FigmaService
import auth.FigmaAuthenticator
import client.FigmaClient
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import model.FigmaNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import util.JsonUtil

class FigmaServiceTest {

    @Test
    fun getNodes_withTestData_returnJsonStringOfNodes(): Unit = runBlocking {
        // arrange
        val mockAuthenticator = mockk<FigmaAuthenticator>()
        val mockClient = mockk<FigmaClient>()

        coEvery { mockAuthenticator.getAccessToken() } returns "test_token"
        coEvery { mockClient.fetchNodes(any(),any()) } returns FigmaNode(nodes=mapOf("1:1" to FigmaNode(name="test")))

        val figmaServiceWithMock = FigmaService(mockAuthenticator.getAccessToken(), mockAuthenticator, mockClient)

        // act
        val nodesResult = figmaServiceWithMock.getNodes("test_file", listOf("1:1"))

        // assert
        assertEquals(nodesResult, JsonUtil.toJson(FigmaNode(nodes=mapOf("1:1" to FigmaNode(name="test")))))
    }

    @Test
    fun getFile_withTestData_returnJsonStringOfFigmaFile(): Unit = runBlocking{
        // arrange
        val mockAuthenticator = mockk<FigmaAuthenticator>()
        val mockClient = mockk<FigmaClient>()

        coEvery { mockAuthenticator.getAccessToken() } returns "test_token"
        coEvery { mockClient.fetchFile(any()) } returns FigmaNode(name="testFile")

        val figmaServiceWithMock = FigmaService(mockAuthenticator.getAccessToken(), mockAuthenticator, mockClient)

        // act
        val fileResult = figmaServiceWithMock.getFile("test_file")

        // assert
        assertEquals(fileResult, JsonUtil.toJson(FigmaNode(name="testFile")))
    }

}
