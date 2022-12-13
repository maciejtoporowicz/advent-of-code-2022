import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day12Test {
    @Test
    fun testPart01() {
        val reader = { ResourceReader.reader("day12.txt") }

        assertEquals(31, Day12().runPart01(reader))
    }

    @Test
    fun testPart02() {
        val reader = { ResourceReader.reader("day12.txt") }

        assertEquals(29, Day12().runPart02(reader))
    }
}