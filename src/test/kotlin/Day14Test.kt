import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {
    @Test
    fun testPart01() {
        val lines = { ResourceReader.readLines("day14.txt") }

        assertEquals(24, Day14().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = { ResourceReader.readLines("day14.txt") }

        assertEquals(93, Day14().runPart02(lines))
    }
}