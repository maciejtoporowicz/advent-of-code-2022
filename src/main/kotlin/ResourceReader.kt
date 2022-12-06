object ResourceReader {
    fun read(path: String) = this::class.java.getResourceAsStream(path)?.bufferedReader()?.lines()!!

    fun reader(path: String) = this::class.java.getResourceAsStream(path)?.bufferedReader()!!
}