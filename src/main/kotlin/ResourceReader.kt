object ResourceReader {
    fun readLines(path: String) = this::class.java.getResourceAsStream(path)?.bufferedReader()?.lines()!!

    fun reader(path: String) = this::class.java.getResourceAsStream(path)?.bufferedReader()!!
}