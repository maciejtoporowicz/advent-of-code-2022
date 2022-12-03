import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day03Test {
    @Test
    fun testPart01() {
        val lines = ResourceReader.read("day03.txt")

        assertEquals(157, Day03().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = ResourceReader.read("day03.txt")

        assertEquals(70, Day03().runPart02(lines))
    }
}