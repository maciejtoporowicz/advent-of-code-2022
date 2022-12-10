import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.readLines("day10.txt") }

    println(Day10().runPart01(lines()))
    println(Day10().runPart02(lines()))
}

class Day10 {
    fun runPart01(lines: Stream<String>): Int {
        var sumOfSignalStrengths = 0

        val cyclesWhenSignalNeedsToBeMeasured = setOf(20, 60, 100, 140, 180, 220)

        val cpu = CPU(object : CPU.XListener {
            override fun notify(cycle: Int, x: Int) {
                if(cyclesWhenSignalNeedsToBeMeasured.contains(cycle)) {
                    sumOfSignalStrengths += cycle * x
                }
            }
        })

        runInstructions(lines, cpu)

        return sumOfSignalStrengths
    }

    fun runPart02(lines: Stream<String>): String {
        val crtOutput = StringBuilder()

        val cpu = CPU(object : CPU.XListener {
            override fun notify(cycle: Int, x: Int) {
                val crtDrawingPosition = (cycle - 1) % 40

                if(x - 1 <= crtDrawingPosition && crtDrawingPosition <= x + 1) {
                    crtOutput.append("#")
                } else {
                    crtOutput.append(".")
                }

                if(crtDrawingPosition == 39) {
                    crtOutput.append("\n")
                }
            }
        })

        runInstructions(lines, cpu)

        return crtOutput.toString()
    }

    private fun runInstructions(lines: Stream<String>, cpu: CPU) {
        lines.forEach { line ->
            val (instruction, arg) = line
                .split(" ")
                .let { if (it.size == 2) it[0] to Integer.parseInt(it[1]) else it[0] to null }

            when (instruction) {
                "noop" -> cpu.runInstruction(CPU.Instruction.Noop())
                "addx" -> cpu.runInstruction(CPU.Instruction.AddX(arg!!))
            }
        }
    }

    class CPU(private val xListener: XListener) {
        private var X = AtomicInteger(1)
        private var cycle: Int = 0

        fun runInstruction(instruction: Instruction) {
            (1..instruction.requiredCycles).forEach { instructionCycle ->
                cycle++

                xListener.notify(cycle, X.get())

                instruction.run(instructionCycle, X)
            }
        }

        interface XListener {
            fun notify(cycle: Int, x: Int)
        }

        sealed class Instruction(val requiredCycles: Int) {
            abstract fun run(instructionCycle: Int, X: AtomicInteger)

            class Noop : Instruction(1) {
                override fun run(instructionCycle: Int, X: AtomicInteger) {
                    // noop
                }
            }

            class AddX(private val arg: Int) : Instruction(2) {
                override fun run(instructionCycle: Int, X: AtomicInteger) {
                    if (instructionCycle == 2) {
                        X.addAndGet(arg)
                    }
                }
            }
        }
    }
}