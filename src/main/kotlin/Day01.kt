import java.util.stream.Stream
import kotlin.math.max

fun main() {
    val lines = { ResourceReader.read("day01.txt") }

    println(Day01().runPart1(lines()))
    println(Day01().runPart2(lines()))
}

class Day01 {
    fun runPart1(lines: Stream<String>): Int {
        var partialSumOfCalories = 0
        var maxCalories = 0

        lines.forEach { line ->
            if(line.isBlank()) {
                maxCalories = max(maxCalories, partialSumOfCalories)
                partialSumOfCalories = 0
            } else {
                partialSumOfCalories += line.toInt()
            }
        }

        return max(maxCalories, partialSumOfCalories)
    }

    fun runPart2(lines: Stream<String>): Int {
        var partialSumOfCalories = 0
        var topThreeMaxCalories = listOf<Int>()

        lines.forEach { line ->
            if(line.isBlank()) {
                topThreeMaxCalories = topThreeMaxCalories
                    .plus(partialSumOfCalories)
                    .sortedDescending()
                    .take(3)
                partialSumOfCalories = 0
            } else {
                partialSumOfCalories += line.toInt()
            }
        }

        return topThreeMaxCalories
            .plus(partialSumOfCalories)
            .sortedDescending()
            .take(3)
            .sum()
    }
}