import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day02Test {
    @Test
    fun testPart01() {
        val lines = ResourceReader.readLines("day02.txt")

        assertEquals(15, Day02().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = ResourceReader.readLines("day02.txt")

        assertEquals(12, Day02().runPart02(lines))
    }
}