import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day01Test {
    @Test
    fun testPart01() {
        val lines = ResourceReader.read("day01.txt")

        assertEquals(24000, Day01().runPart1(lines))
    }

    @Test
    fun testPart02() {
        val lines = ResourceReader.read("day01.txt")

        assertEquals(45000, Day01().runPart2(lines))
    }
}