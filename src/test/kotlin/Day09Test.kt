import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day09Test {
    @Test
    fun testPart01() {
        val lines = listOf(
            "R 4",
            "U 4",
            "L 3",
            "D 1",
            "R 4",
            "D 1",
            "L 5",
            "R 2"
        ).stream()

        assertEquals(13, Day09().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = listOf(
            "R 5",
            "U 8",
            "L 8",
            "D 3",
            "R 17",
            "D 10",
            "L 25",
            "U 20"
        ).stream()

        assertEquals(36, Day09().runPart02(lines))
    }
}