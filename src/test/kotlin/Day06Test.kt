import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day06Test {
    @Test
    fun testPart01() {
        val day06 = Day06()

        assertAll(
            { assertEquals(7, day06.runPart01 { "mjqjpqmgbljsphdztnvjfqwrcgsmlb".reader() }) },
            { assertEquals(5, day06.runPart01 { "bvwbjplbgvbhsrlpgdmjqwftvncz".reader() }) },
            { assertEquals(6, day06.runPart01 { "nppdvjthqldpwncqszvftbrmjlhg".reader() }) },
            { assertEquals(10, day06.runPart01 { "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg".reader() }) },
            { assertEquals(11, day06.runPart01 { "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw".reader() }) }
        )
    }

    @Test
    fun testPart02() {
        val day06 = Day06()

        assertAll(
            { assertEquals(19, day06.runPart02 { "mjqjpqmgbljsphdztnvjfqwrcgsmlb".reader() }) },
            { assertEquals(23, day06.runPart02 { "bvwbjplbgvbhsrlpgdmjqwftvncz".reader() }) },
            { assertEquals(23, day06.runPart02 { "nppdvjthqldpwncqszvftbrmjlhg".reader() }) },
            { assertEquals(29, day06.runPart02 { "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg".reader() }) },
            { assertEquals(26, day06.runPart02 { "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw".reader() }) }
        )
    }
}