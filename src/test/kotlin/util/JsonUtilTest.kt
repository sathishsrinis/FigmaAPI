package co.`in`.acmesense.util

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.slf4j.Logger
import util.JsonUtil
import java.io.File
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonUtilTest {
    @Test
    fun toJson_default_serializesDataToJsonString() {
        // arrange
        val data = TestData("Test", 123)
        val expectedJson = """{"name":"Test","number":123}"""

        // act
        val actualJson = JsonUtil.toJson(data)

        // assert
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun saveJsonToFile_default_savesDataToFile(@TempDir tempDir: Path) {
        // arrange
        val loggerMock = mockk<Logger>(relaxed = true)
        val fileName = tempDir.resolve("test.json").toString()
        val data = """{"test":"value"}"""

        // act
        JsonUtil.saveJsonToFile(fileName, data, loggerMock)
        val file = File(fileName)

        // assert
        assertTrue(file.exists())
        assertEquals(data, file.readText())
        verify { loggerMock.info("Response saved to file: ${file.absolutePath}") }
    }

    @Test
    fun saveJsonToFile_invalidFileName_handlesFileSaveException(@TempDir tempDir: Path) {
        // arrange
        val loggerMock = mockk<Logger>(relaxed = true)
        val invalidFileName = tempDir.resolve("invalid/test.json").toString()
        val data = """{"test":"value"}"""

        // act
        JsonUtil.saveJsonToFile(invalidFileName, data, loggerMock)

        // assert
        verify { loggerMock.error(any<String>(), any()) }
        confirmVerified(loggerMock)
        val capturedError = slot<String>()
        verify { loggerMock.error(capture(capturedError), any()) }
        assertTrue(capturedError.captured.startsWith("Error saving JSON to file:"))
    }

}

@kotlinx.serialization.Serializable
data class TestData(val name: String, val number: Int)