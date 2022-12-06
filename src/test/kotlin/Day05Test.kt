import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day05Test {
    @Test
    fun testPart01() {
        val lines = ResourceReader.readLines("day05.txt")

        assertEquals("CMZ", Day05().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = ResourceReader.readLines("day05.txt")

        assertEquals("MCD", Day05().runPart02(lines))
    }
}