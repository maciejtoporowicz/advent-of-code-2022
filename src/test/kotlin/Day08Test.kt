import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day08Test {
    @Test
    fun testPart01() {
        val readerProvider = { ResourceReader.reader("day08.txt") }

        assertEquals(21, Day08().runPart01(readerProvider))
    }

    @Test
    fun testPart02() {
        val readerProvider = { ResourceReader.reader("day08.txt") }

        assertEquals(8, Day08().runPart02(readerProvider))
    }
}