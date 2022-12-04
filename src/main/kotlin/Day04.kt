import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.read("day04.txt") }

    println(Day04().runPart01(lines()))
    println(Day04().runPart02(lines()))
}

class Day04 {
    fun runPart01(lines: Stream<String>): Int {
        var numOfPairsWhereOneFullyContainsTheOther = 0

        lines.forEach {
            val (rangeA, rangeB) = parse(it)
            if (rangeA.contains(rangeB) || rangeB.contains(rangeA)) {
                numOfPairsWhereOneFullyContainsTheOther++
            }
        }

        return numOfPairsWhereOneFullyContainsTheOther
    }

    private fun parse(line: String): Pair<Range, Range> {
        val rangesAsString = line.split(",")

        val rangeA = asRange(rangesAsString[0])
        val rangeB = asRange(rangesAsString[1])

        return rangeA to rangeB
    }

    private fun asRange(rangeAsString: String): Range {
        return rangeAsString.split("-").let { Range(it[0].toInt(), it[1].toInt()) }
    }

    fun runPart02(lines: Stream<String>): Int {
        var numOfPairsThatOverlapEachOther = 0

        lines.forEach {
            val (rangeA, rangeB) = parse(it)
            if (rangeA.hasOverlapWith(rangeB)) {
                numOfPairsThatOverlapEachOther++
            }
        }

        return numOfPairsThatOverlapEachOther
    }

    data class Range(val a: Int, val b: Int) {
        fun contains(other: Range): Boolean {
            return other.a >= a && other.b <= b
        }

        fun hasOverlapWith(other: Range): Boolean {
            return (other.a in a..b)
                    || (other.b in a..b)
                    || (a in other.a..other.b)
                    || (b in other.a..other.b)
        }
    }
}
