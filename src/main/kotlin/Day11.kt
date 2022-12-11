import java.util.*
import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.readLines("day11.txt") }

    println(Day11().runPart01(lines()))
    println(Day11().runPart02(lines()))
}

typealias WorryLevel = Int
typealias MonkeyNumber = Int

class Day11 {
    fun runPart01(lines: Stream<String>): Int {
        return run(lines, 3, 20)
    }

    fun runPart02(lines: Stream<String>): Int {
        return run(lines, 1, 10000)
    }

    private fun run(lines: Stream<String>, worryLevelDivisor: Int, numOfRounds: Int): Int {
        val monkeys = parseMonkeys(lines)

        var round = 1

        repeat(numOfRounds) {
            monkeys.values.forEach { monkey ->
                var itemToInspect = monkey.takeOutNextItem()

                while (itemToInspect != null) {
                    val worryLevel = itemToInspect
                        .let { monkey.inspectItemWith(it) }
                        .let { it / worryLevelDivisor }

                    val monkeyToThrowItemTo = monkey.whereToThrowItemWith(worryLevel)
                        .let { monkeys[it]!! }

                    monkeyToThrowItemTo.catchItemWith(worryLevel)

                    itemToInspect = monkey.takeOutNextItem()
                }
            }

            println("After round $round")
            monkeys.entries
                .sortedBy { (k, _) -> k }
                .forEach { println("Monkey ${it.value.number}: ${it.value.peek().joinToString(separator = ", ")}") }
            println()

//            if (setOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000).contains(round)) {
//                println("After roun $round")
//                monkeys.values.forEach {
//                    println("Monkey ${it.number} inspected items ${it.inspections()} times.")
//                }
//                println("")
//                println("===================================================")
//                println()
//            }

            round++
        }

        val topTwoActiveMonkeys = monkeys
            .values
            .sortedByDescending { it.inspections() }
            .take(2)

        return topTwoActiveMonkeys[0].inspections() * topTwoActiveMonkeys[1].inspections()
    }

    private fun parseMonkeys(lines: Stream<String>): Map<MonkeyNumber, Monkey> {
        val monkeys = mutableListOf<Monkey>()

        var monkeyNumber: MonkeyNumber? = null
        var startingItems: List<WorryLevel>? = null
        var operationOperator: String? = null
        var operationOperand: ((WorryLevel) -> Int)? = null
        var test: Int? = null
        var testTrue: MonkeyNumber? = null
        var testFalse: MonkeyNumber? = null

        lines.forEach { untrimmedLine ->
            val line = untrimmedLine.trim()

            if (line.startsWith("Monkey")) {
                monkeyNumber = line
                    .replace("Monkey ", "")
                    .replace(":", "")
                    .toInt()
            } else if (line.startsWith("Starting items")) {
                startingItems = line.replace("Starting items: ", "")
                    .split(", ")
                    .map { it.toInt() }
            } else if (line.startsWith("Operation")) {
                line.replace("Operation: new = old ", "")
                    .split(" ")
                    .also { args ->
                        operationOperator = args[0]
                        operationOperand = when (args[1]) {
                            "old" -> { worryLevel -> worryLevel }
                            else -> { _ -> args[1].toInt() }
                        }
                    }
            } else if (line.startsWith("Test")) {
                test = line
                    .replace("Test: divisible by ", "")
                    .toInt()
            } else if (line.startsWith("If true")) {
                testTrue = line
                    .replace("If true: throw to monkey ", "")
                    .toInt()
            } else if (line.startsWith("If false")) {
                testFalse = line
                    .replace("If false: throw to monkey ", "")
                    .toInt()

                monkeys.add(
                    Monkey(
                        number = monkeyNumber!!,
                        items = LinkedList(startingItems!!),
                        inspectionOperand = operationOperand!!,
                        inspectionOperator = operationOperator!!,
                        test = test!!,
                        testTrue = testTrue!!,
                        testFalse = testFalse!!
                    )
                )
            }
        }

        return monkeys.associateBy { it.number }.toSortedMap(Comparator.naturalOrder())
    }

    class Monkey(
        val number: MonkeyNumber,
        private val items: Deque<WorryLevel>,
        inspectionOperator: String,
        inspectionOperand: (WorryLevel) -> Int,
        val test: Int,
        testTrue: MonkeyNumber,
        testFalse: MonkeyNumber
    ) {
        private val inspect: (WorryLevel) -> WorryLevel = when (inspectionOperator) {
            "*" -> { oldWorryLevel -> oldWorryLevel * inspectionOperand(oldWorryLevel) }
            "+" -> { oldWorryLevel -> oldWorryLevel + inspectionOperand(oldWorryLevel) }
            else -> throw IllegalArgumentException("Unknown operator $inspectionOperator")
        }

        private val whereToThrow: (WorryLevel) -> MonkeyNumber =
            { worryLevel -> if (worryLevel % test == 0) testTrue else testFalse }

        private var inspections = 0

        fun peek() = items.toList()

        fun inspectItemWith(worrylevel: WorryLevel): WorryLevel {
            inspections++
            return inspect(worrylevel)
        }

        fun takeOutNextItem(): WorryLevel? = if (items.isEmpty()) null else items.pollFirst()

        fun catchItemWith(worryLevel: WorryLevel) = items.offerLast(worryLevel)

        fun inspections() = inspections

        fun whereToThrowItemWith(worryLevel: WorryLevel): MonkeyNumber = whereToThrow(worryLevel)
    }
}