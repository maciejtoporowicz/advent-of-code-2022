import java.lang.Integer.max
import java.util.*
import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.readLines("day13.txt") }

    println(Day13().runPart01(lines))
    println(Day13().runPart02(lines))
}

class Day13 {
    fun runPart01(lines: () -> Stream<String>): Int {
        val toCompare = parse(lines())

        return toCompare
            .mapIndexed { index, data -> index + 1 to data.isOrdered() }
            .filter { (_, isOrdered) -> isOrdered }
            .sumOf { it.first }
    }

    fun runPart02(lines: () -> Stream<String>): Int {
        val sortedSignal = Stream.concat(lines(), Stream.of("[[2]]", "[[6]]"))
            .filter { it.isNotEmpty() }
            .map { parse(it) }
            .sorted()
            .toList()

        return (sortedSignal.indexOfFirst { it.toString() == "[[2]]" } + 1) *
                (sortedSignal.indexOfFirst { it.toString() == "[[6]]" } + 1)
    }

    private fun parse(lines: Stream<String>): List<ComparisonData> {
        var lineNumber = 0
        var left: Item.ListOfItems? = null
        var right: Item.ListOfItems? = null

        val parsed = mutableListOf<ComparisonData>()

        lines.forEach { line ->
            when (lineNumber % 3) {
                0 -> left = parse(line)
                1 -> right = parse(line)
                2 -> parsed.add(ComparisonData(left!!, right!!))
            }
            lineNumber++
        }

        parsed.add(ComparisonData(left!!, right!!))

        return parsed
    }

    private fun parse(string: String): Item.ListOfItems {
        val fifo = LinkedList<String>()

        val partialParsed = StringBuilder()

        string.forEach { char ->
            if (char == '[') {
                fifo.offer("[")
            } else if (char == ']') {
                if (partialParsed.isNotEmpty()) {
                    fifo.add(partialParsed.toString())
                    partialParsed.setLength(0)
                }
                fifo.offer("]")
            } else if (char.isDigit()) {
                partialParsed.append(char)
            } else if (char == ',') {
                fifo.add(partialParsed.toString())
                partialParsed.setLength(0)
            } else {
                throw IllegalArgumentException("Unknown char $char")
            }
        }

        return parse(fifo).let {
            if (it !is Item.ListOfItems) {
                throw IllegalStateException("Expected a list here")
            } else {
                it
            }
        }
    }

    private fun parse(fifo: LinkedList<String>): Item {
        val popped = fifo.pop()

        if (popped == "[") {
            return parseList(fifo)
        } else {
            throw IllegalStateException("popped = [$popped], shouldn't have happened")
        }
    }

    private fun parseList(fifo: LinkedList<String>): Item {
        val items = mutableListOf<Item>()

        var popped = fifo.pop()

        while (popped != "]") {
            if (popped.matches(Regex("\\d+"))) {
                items.add(Item.SingleInteger(popped.toInt()))
            } else if (popped == "[") {
                items.add(parseList(fifo))
            }

            popped = fifo.pop()
        }

        return Item.ListOfItems(items)
    }

    data class ComparisonData(val left: Item.ListOfItems, val right: Item.ListOfItems) {
        fun isOrdered(): Boolean {
            return left <= right
        }
    }

    sealed class Item {

        data class SingleInteger(val value: Int) : Item(), Comparable<SingleInteger> {
            override fun toString(): String {
                return "$value"
            }

            override fun compareTo(other: SingleInteger): Int {
                return this.value - other.value
            }
        }

        data class ListOfItems(val values: List<Item>) : Item(), Comparable<ListOfItems> {
            override fun toString(): String {
                return "[${values.joinToString(",")}]"
            }

            override fun compareTo(other: ListOfItems): Int {
                val left = this.values
                val right = other.values

                if (left.isEmpty() && right.isEmpty()) {
                    return 0
                } else if (left.isEmpty()) {
                    return -1
                } else if (right.isEmpty()) {
                    return 1
                }

                val maxIndex = max(max(left.size, right.size) - 1, 0)

                (0..maxIndex).forEach { index ->
                    val leftExists = left.size > index
                    val rightExists = right.size > index

                    if (!leftExists && rightExists) {
                        return -1
                    } else if (leftExists && !rightExists) {
                        return 1
                    } else if (leftExists) {
                        val leftItem = left[index]
                        val rightItem = right[index]

                        val comparisonResult = compare(leftItem, rightItem)

                        if (comparisonResult > 0) {
                            return 1
                        } else if (comparisonResult < 0) {
                            return -1
                        }
                    }
                }
                return 0
            }

            private fun compare(leftItem: Item, rightItem: Item): Int {
                return if (leftItem is SingleInteger && rightItem is SingleInteger) {
                    leftItem.compareTo(rightItem)
                } else if (leftItem is ListOfItems && rightItem is ListOfItems) {
                    leftItem.compareTo(rightItem)
                } else if (leftItem is SingleInteger && rightItem is ListOfItems) {
                    ListOfItems(listOf(leftItem)).compareTo(rightItem)
                } else if (leftItem is ListOfItems && rightItem is SingleInteger) {
                    leftItem.compareTo(ListOfItems(listOf(rightItem)))
                } else {
                    throw RuntimeException("shouldn't have happened")
                }
            }
        }
    }
}