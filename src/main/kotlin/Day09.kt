import java.util.stream.Stream
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val lines = { ResourceReader.readLines("day09.txt") }

    println(Day09().runPart01(lines()))
    println(Day09().runPart02(lines()))
}

class Day09 {
    fun runPart01(lines: Stream<String>): Int {
        var head = Point(0, 0)
        var tail = Point(0, 0)

        val pointsVisitedByTail = mutableSetOf(tail)

        runCommands(lines) { headMovementStrategy ->
            head = headMovementStrategy(head)
            tail = chase(tail, head)
            pointsVisitedByTail.add(tail)
        }

        return pointsVisitedByTail.count()
    }

    fun runPart02(lines: Stream<String>): Int {
        val numOfKnots = 10
        val headIndex = 0

        val knots = (0 until numOfKnots)
            .map { Point(0, 0) }
            .toMutableList()

        val pointsVisitedByTail = mutableSetOf(knots.last())

        runCommands(lines) { headMovementStrategy ->
            (0 until numOfKnots).forEach { index ->
                if (index == headIndex) {
                    knots[headIndex] = headMovementStrategy(knots[headIndex])
                } else {
                    val precedingKnotIndex = index - 1
                    knots[index] = chase(knots[index], knots[precedingKnotIndex])
                }
            }
            pointsVisitedByTail.add(knots.last())
        }

        return pointsVisitedByTail.count()
    }

    private fun chase(tail: Point, head: Point): Point {
        return if (tail.distanceTo(head) >= 2.0) {
            val diffX = head.x - tail.x
            val diffY = head.y - tail.y

            tail
                .let { if (diffX > 0) it.right() else if (diffX == 0) it else it.left() }
                .let { if (diffY > 0) it.up() else if (diffY == 0) it else it.down() }
        } else {
            tail
        }
    }

    private fun runCommands(lines: Stream<String>, move: ((Point) -> Point) -> Unit) {
        lines.forEach { line ->
            val (direction, distance) = line.split(" ").let { it[0] to it[1].toInt() }

            val headMovementStrategy: (Point) -> Point = when (direction) {
                "U" -> { p: Point -> p.up() }
                "R" -> { p: Point -> p.right() }
                "D" -> { p: Point -> p.down() }
                "L" -> { p: Point -> p.left() }
                else -> throw IllegalArgumentException("Unknown direction $direction")
            }

            repeat(distance) { move(headMovementStrategy) }

        }
    }

    data class Point(val x: Int, val y: Int) {
        fun up(): Point = copy(y = y + 1)
        fun right(): Point = copy(x = x + 1)
        fun down(): Point = copy(y = y - 1)
        fun left(): Point = copy(x = x - 1)

        fun distanceTo(other: Point) = sqrt(
            (other.x - this.x).toDouble().pow(2.0)
                    + (other.y - this.y).toDouble().pow(2.0)
        )
    }
}