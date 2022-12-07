import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day07Test {
    @Test
    fun testPart01() {
        val lines = ResourceReader.readLines("day07.txt")

        assertEquals(95437, Day07().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = ResourceReader.readLines("day07.txt")

        assertEquals(24933642, Day07().runPart02(lines))
    }
}