import java.util.*
import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.read("day05.txt") }

    println(Day05().runPart01(lines()))
    println(Day05().runPart02(lines()))
}

class Day05 {
    fun runPart01(lines: Stream<String>): String {
        val stacksParser = StacksParser()

        lines.forEach LineParsing@{ line ->
            val stacks = if(!stacksParser.isParsed()) {
                stacksParser.consume(line)
                return@LineParsing
            } else {
                stacksParser.getStacks()
            }

            val (howMany, from, to) = parseCommand(line)

            repeat(howMany) {
                stacks[from]!!
                    .pollFirst()
                    .also { stacks[to]!!.offerFirst(it) }
            }
        }

        return prepareResult(stacksParser)
    }

    fun runPart02(lines: Stream<String>): String {
        val stacksParser = StacksParser()

        lines.forEach LineParsing@{ line ->
            val stacks = if(!stacksParser.isParsed()) {
                stacksParser.consume(line)
                return@LineParsing
            } else {
                stacksParser.getStacks()
            }

            val (howMany, from, to) = parseCommand(line)

            val pickedUpCrates = LinkedList<String>()

            repeat(howMany) {
                stacks[from]!!
                    .pollFirst()
                    .also { pickedUpCrates.offerFirst(it) }
            }

            repeat(howMany) {
                stacks[to]!!.offerFirst(pickedUpCrates.pollFirst())
            }
        }

        return prepareResult(stacksParser)
    }

    private fun prepareResult(stacksParser: StacksParser) = stacksParser
        .getStacks()
        .entries
        .sortedBy { it.key }
        .joinToString(separator = "") { it.value.peekFirst() }

    private fun parseCommand(line: String): Triple<Int, Int, Int> {
        val commandValues = Regex("move (\\d+) from (\\d+) to (\\d+)")
            .matchEntire(line)
            .let { it ?: throw RuntimeException("Regex failed") }
            .groupValues

        val howMany = Integer.parseInt(commandValues[1])
        val from = Integer.parseInt(commandValues[2])
        val to = Integer.parseInt(commandValues[3])

        return Triple(howMany, from, to)
    }

    class StacksParser {
        private val stacks = mutableMapOf<Int, Deque<String>>()
        private var parsed = false

        fun consume(line: String) {
            if (line.startsWith(" 1")) {
                return // header line, skip
            } else if (line.isBlank()) {
                parsed = true
                return // empty newline after stacks
            } else {
                var parsedLine = line
                var numberOfParsedCrate = 1
                do {
                    val crateContent = parsedLine.substring(0, 3)[1].toString() // read [X] and take out X
                    if (crateContent.isNotBlank()) {
                        stacks.compute(numberOfParsedCrate) { _, stack ->
                            (stack ?: LinkedList())
                                .also { it.offerLast(crateContent) }
                        }
                    }
                    numberOfParsedCrate++
                    parsedLine = if (parsedLine.length > 3) {
                        parsedLine.substring(4) // skip space between columns
                    } else {
                        ""
                    }
                } while (parsedLine.isNotEmpty())
            }
        }

        fun isParsed() = parsed

        fun getStacks(): Map<Int, Deque<String>> = if (parsed) stacks else throw RuntimeException("Stack")
    }
}
