import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day13Test {
    @Test
    fun testPart01() {
        val lines = { ResourceReader.readLines("day13.txt") }

        assertEquals(13, Day13().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = { ResourceReader.readLines("day13.txt") }

        assertEquals(140, Day13().runPart02(lines))
    }
}