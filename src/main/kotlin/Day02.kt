import java.lang.IllegalStateException
import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.read("day02.txt") }

    println(Day02().runPart01(lines()))
    println(Day02().runPart02(lines()))
}

class Day02 {
    fun runPart01(lines: Stream<String>): Int {
        var sum = 0
        lines.forEach { line -> sum += determineScorePart01(line, this::evalPart01) }
        return sum
    }

    private fun determineScorePart01(line: String, evalFn: (String, String) -> Int) =
        line
            .split(" ")
            .let { evalFn(it[0], it[1]) }

    private fun evalPart01(opponent: String, me: String): Int {
        val opponentShape = shapeByCode[opponent]!!
        val myShape = shapeByCode[me]!!

        val outcome = outcomeCalculation[(myShape to opponentShape)]!!
        val shapeScore = shapeScore[myShape]!!

        return outcome + shapeScore
    }

    fun runPart02(lines: Stream<String>): Int {
        var sum = 0
        lines.forEach { line -> sum += determineScorePart01(line, this::evalPart02) }
        return sum
    }

    private fun evalPart02(opponent: String, expected: String): Int {
        val opponentShape = shapeByCode[opponent]!!
        val expectedOutcome = when (expected) {
                "X" -> 0
                "Y" -> 3
                "Z" -> 6
                else -> throw IllegalArgumentException("Unexpected sign $expected")
        }

        val shapesForExpectedOutcome = outcomeCalculation
            .filter { (_, outcome) -> outcome == expectedOutcome }
            .filterKeys { (_, opponent) -> opponent == opponentShape }
            .keys
            .also { if(it.size > 1) throw IllegalStateException("Only one entry expected") }
            .first()

        val myShape = shapesForExpectedOutcome.first

        val outcome = outcomeCalculation[shapesForExpectedOutcome]!!
        val shapeScore = shapeScore[myShape]!!

        return outcome + shapeScore
    }

    companion object {
        private val shapeByCode = mapOf(
            "A" to "Rock",
            "B" to "Paper",
            "C" to "Scissors",
            "X" to "Rock",
            "Y" to "Paper",
            "Z" to "Scissors"
        )

        private val outcomeCalculation = mapOf(
            ("Rock" to "Rock") to 3,
            ("Rock" to "Paper") to 0,
            ("Rock" to "Scissors") to 6,
            ("Paper" to "Rock") to 6,
            ("Paper" to "Paper") to 3,
            ("Paper" to "Scissors") to 0,
            ("Scissors" to "Rock") to 0,
            ("Scissors" to "Paper") to 6,
            ("Scissors" to "Scissors") to 3
        )

        private val shapeScore = mapOf(
            "Rock" to 1,
            "Paper" to 2,
            "Scissors" to 3
        )
    }
}