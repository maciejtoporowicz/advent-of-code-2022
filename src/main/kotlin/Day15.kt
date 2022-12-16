import java.lang.Integer.min
import java.util.*
import java.util.stream.Stream
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val lines = { ResourceReader.readLines("day15.txt") }

    println(Day15().runPart01(lines, 2000000))
    println(Day15().runPart02(lines, 4000000))
}

class Day15 {
    fun runPart01(lines: () -> Stream<String>, y: Int): Int {
        val sensors = parseSensors(lines())

        val rangeSet = sensors.fold(
            RangeSet(TreeSet())
        ) { acc, sensor -> sensor.coveredRangeX(y)?.let { acc.plus(it) } ?: acc }

        val beaconsOnY = sensors.filter { it.closestBeacon.y == y }.map { it.closestBeacon }.toSet()
        val sensorsOnY = sensors.filter { it.position.y == y }.map { it.position }.toSet()

        val numOfBeaconsInRangeSet = beaconsOnY.count { rangeSet.contains(it.x) }
        val numOfSensorsInRangeSet = sensorsOnY.count { rangeSet.contains(it.x) }

        return rangeSet.countOfCoveredX() - numOfBeaconsInRangeSet - numOfSensorsInRangeSet
    }

    fun runPart02(lines: () -> Stream<String>, maxPositionInEitherAxis: Int): Long {
        val sensors = parseSensors(lines())

        val rangeWhereSignalIsExpected = Range(0, maxPositionInEitherAxis)

        (0..maxPositionInEitherAxis).forEach { y ->
            val rangeSet = sensors.fold(
                RangeSet(TreeSet())
            ) { acc, sensor -> sensor.coveredRangeX(y)?.let { acc.plus(it) } ?: acc }

            if (rangeSet.containsHole(rangeWhereSignalIsExpected)) {
                return (rangeSet.getHole(rangeWhereSignalIsExpected).toLong() * 4000000L) + y.toLong()
            }
        }

        throw RuntimeException("Not found")
    }

    private fun parseSensors(lines: Stream<String>): List<Sensor> {
        val regex = Regex("x=(-?\\d+). y=(-?\\d+)")

        return lines.map { line ->
            val (sensorsPart, beaconPart) = line.split("closest")
            val sensorsPartFindings = regex.find(sensorsPart)!!.groupValues
            val beaconPartFindings = regex.find(beaconPart)!!.groupValues

            Sensor(
                Point(sensorsPartFindings[1].toInt(), sensorsPartFindings[2].toInt()),
                Point(beaconPartFindings[1].toInt(), beaconPartFindings[2].toInt())
            )
        }.toList()
    }

    data class Point(val x: Int, val y: Int)

    data class Sensor(val position: Point, val closestBeacon: Point) {

        val coveredDistance = abs(position.x - closestBeacon.x) + abs(position.y - closestBeacon.y)

        fun coveredRangeX(y: Int): Range? {
            // | Sx - x | + | Sy - y | <= coveredDistance
            // | Sx - x | <= coveredDistance - | Sy - y |
            //
            // x1:
            // Sx - x <= coveredDistance - | Sy - y |
            // -x <= coveredDistance - | Sy - y | - Sx
            // x >= -1 * (coveredDistance - | Sy - y | - Sx)
            //
            // x2:
            // Sx - x >= -1 * (coveredDistance - | Sy - y |)
            // -x >= -1 * (coveredDistance - | Sy - y |) - Sx
            // x <= coveredDistance - | Sy - y | + Sx
            //
            // range = min(x1, x2) .. max(x1, x2)

            val x1 = -1 * (coveredDistance - abs(position.y - y) - position.x)
            val x2 = coveredDistance - abs(position.y - y) + position.x

            val rangeForX1 = Range(x1, Int.MAX_VALUE)
            val rangeForX2 = Range(Int.MIN_VALUE, x2)

            return if (rangeForX1.overlaps(rangeForX2)) {
                Range(
                    min = min(x1, x2),
                    max = max(x1, x2)
                )
            } else {
                null
            }
        }
    }

    data class RangeSet(val ranges: SortedSet<Range>) {
        fun plus(rangeToAdd: Range): RangeSet {
            val (existingToBeJoinedTogether, notToBeJoinedTogether) = ranges.partition { range ->
                rangeToAdd.overlaps(range) || rangeToAdd.joins(range)
            }

            val toBeJoinedTogether = existingToBeJoinedTogether.plus(rangeToAdd)

            val minInToBeJoinedTogether = toBeJoinedTogether.minOf { it.min }
            val maxInToBeJoinedTogether = toBeJoinedTogether.maxOf { it.max }

            return RangeSet(
                notToBeJoinedTogether
                    .plus(Range(minInToBeJoinedTogether, maxInToBeJoinedTogether))
                    .toSortedSet { r1, r2 -> r1.min.compareTo(r2.min) }
            )
        }

        fun countOfCoveredX() = ranges.sumOf { it.max - it.min + 1 }

        fun contains(x: Int): Boolean {
            return ranges.find { x in it.min..it.max } != null
        }

        fun containsHole(range: Range): Boolean {
            val (min, max) = range

            if (ranges.size == 1 && (ranges.first().min > min || ranges.first().max < max)) {
                return true
            } else if (ranges.size > 1 && (ranges.first().min < min || ranges.last().max > max)) {
                return true
            } else {
                return false
            }
        }

        fun getHole(range: Range): Int {
            if (ranges.size == 1) {
                if (ranges.first().min > range.min) {
                    return range.min
                } else if (ranges.first().max < range.max) {
                    return range.max
                }
            } else {
                return ranges.first().max + 1
            }

            throw RuntimeException("No hole in this range.")
        }
    }

    data class Range(val min: Int, val max: Int) {
        init {
            require(min <= max)
        }

        fun joins(other: Range): Boolean {
            return other.min == this.max + 1 || other.max == this.min - 1
        }

        fun overlaps(other: Range): Boolean {
            return other.min in (this.min..this.max)
                    || other.max in (this.min..this.max)
                    || other.min < this.min && other.max > this.max
        }

        fun contains(value: Int) = value in (min..max)
    }
}