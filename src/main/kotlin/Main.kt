fun main() {
    val input = """
        let x =     42
        var y = 3.14
        let z       = x         + y
        let a = x * y
        let b = x -         y
        var c = z /     a
        
        fun abc(a, b) = a + b
        
        fun def() = {       
            let foo = "     Hello"
            let trimmed = foo.trim()
            return trimmed
        }
        
        fun foo() {
            if (x > y) {
                println             (       "x is greater")
            } else {
                println(        "y is greater"  )
            }
        }
        
        when(c) {
            >= 50 -> {
                println("foo")
            }
            10 -> println("bar")
            else -> println("more")
        }
    """.trimIndent()
    val lexer = Lexer(input)
    val tokens = lexer.tokenize()
    tokens.print()
}

