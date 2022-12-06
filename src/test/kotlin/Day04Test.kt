import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day04Test {
    @Test
    fun testPart01() {
        val lines = ResourceReader.readLines("day04.txt")

        assertEquals(2, Day04().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = ResourceReader.readLines("day04.txt")

        assertEquals(4, Day04().runPart02(lines))
    }
}