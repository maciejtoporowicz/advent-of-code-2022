import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day10Test {
    @Test
    fun testPart01() {
        val lines = ResourceReader.readLines("day10.txt")

        assertEquals(13140, Day10().runPart01(lines))
    }

    @Test
    fun testPart02() {
        val lines = ResourceReader.readLines("day10.txt")

        assertEquals("""
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....
            
        """.trimIndent(), Day10().runPart02(lines))
    }
}