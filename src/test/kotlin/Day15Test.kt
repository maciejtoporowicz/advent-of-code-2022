import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test {
    @Test
    fun testPart01() {
        val lines = { ResourceReader.readLines("day15.txt") }

        assertEquals(26, Day15().runPart01(lines, 10))
    }

    @Test
    fun testPart02() {
        val lines = { ResourceReader.readLines("day15.txt") }

        assertEquals(56000011L, Day15().runPart02(lines, 20))
    }
}