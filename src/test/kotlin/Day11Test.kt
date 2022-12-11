import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {
    @Test
    fun testPart01() {
        val lines = ResourceReader.readLines("day11.txt")

        assertEquals(10605, Day11().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = ResourceReader.readLines("day11.txt")

        assertEquals(2713310158, Day11().runPart02(lines))
    }
}