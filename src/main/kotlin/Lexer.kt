typealias TokenList = List<Token>

class Lexer(private val input: String) {
    private var position: Int = 0
    private var lineNumber: Int = 0
    private var charBeforeCurLine: Int = 0
    private val tokens = mutableListOf<Token>()

    private fun getLine() : Int {
        return lineNumber
    }

    private fun getColumn() : Int {
        return position - charBeforeCurLine
    }

    fun tokenize(): TokenList {
        while (position < input.length) {
            when (val char = input[position]) {
                '+' -> addTokenAndAdvance(Token.Plus(getLine(), getColumn()))
                '-' -> if (peekNext() == '>') {
                    position++
                    addTokenAndAdvance(Token.Arrow(getLine(), getColumn()))
                } else {
                    addTokenAndAdvance(Token.Minus(getLine(), getColumn()))
                }
                '*' -> addTokenAndAdvance(Token.Multiply(getLine(), getColumn()))
                '/' -> addTokenAndAdvance(Token.Divide(getLine(), getColumn()))
                '(' -> addTokenAndAdvance(Token.LParen(getLine(), getColumn()))
                ')' -> addTokenAndAdvance(Token.RParen(getLine(), getColumn()))
                '{' -> addTokenAndAdvance(Token.LBrace(getLine(), getColumn()))
                '}' -> addTokenAndAdvance(Token.RBrace(getLine(), getColumn()))
                '[' -> addTokenAndAdvance(Token.LBracket(getLine(), getColumn()))
                ']' -> addTokenAndAdvance(Token.RBracket(getLine(), getColumn()))
                ',' -> addTokenAndAdvance(Token.Comma(getLine(), getColumn()))
                '=' -> addTokenAndAdvance(Token.Equal(getLine(), getColumn()))
                '>' -> addTokenAndAdvance(Token.GreaterThan(getLine(), getColumn()))
                '.' -> addTokenAndAdvance(Token.Dot(getLine(), getColumn()))
                '\n' -> {
                    addTokenAndAdvance(Token.NewLine(getLine(), getColumn()))
                    lineNumber++
                    charBeforeCurLine += position - charBeforeCurLine
                }
                ' ', '\t', '\r' -> position++ // Skip other whitespace
                '"' -> tokenizeStringLiteral()
                else -> {
                    when {
                        char.isDigit() -> tokenizeNumber()
                        char.isLetter() -> tokenizeIdentifierOrKeyword()
                        else -> throw IllegalArgumentException("Unexpected character: $char")
                    }
                }
            }
        }
        return tokens
    }

    private fun addTokenAndAdvance(token: Token) {
        tokens.add(token)
        position++
        charBeforeCurLine++
    }

    private fun peekNext(): Char? {
        return if (position + 1 < input.length) input[position + 1] else null
    }

    private fun tokenizeStringLiteral() {
        val line = getLine()
        val column = getColumn()
        val start = ++position // Skip the opening quote
        while (position < input.length && input[position] != '"') {
            position++
        }
        if (position >= input.length) {
            throw IllegalArgumentException("Unterminated string literal")
        }
        val value = input.substring(start, position)
        position++ // Skip the closing quote
        tokens.add(Token.StringLiteral(value, line, column))
    }

    private fun tokenizeNumber() {
        val line = getLine()
        val column = getColumn()
        val start = position
        while (position < input.length && input[position].isDigit()) {
            position++
        }
        if (position < input.length && input[position] == '.') {
            position++
            while (position < input.length && input[position].isDigit()) {
                position++
            }
            tokens.add(Token.FloatingLiteral(input.substring(start, position).toFloat(), line, column))
        } else {
            tokens.add(Token.IntegerLiteral(input.substring(start, position).toInt(), line, column))
        }
    }

    private fun tokenizeIdentifierOrKeyword() {
        val line = getLine()
        val column = getColumn()
        val start = position
        while (position < input.length && input[position].isLetterOrDigit()) {
            position++
        }
        val token = when (val value = input.substring(start, position)) {
            "let" -> Token.Let(line, column)
            "var" -> Token.Var(line, column)
            "fun" -> Token.Fun(line, column)
            "if" -> Token.If(line, column)
            "else" -> Token.Else(line, column)
            "when" -> Token.When(line, column)
            else -> Token.Identifier(value, line, column)
        }
        tokens.add(token)
    }
}

fun TokenList.print() {
    println("=====================================")
    forEach { if(it !is Token.NewLine) print("${it.toStringComplexToken().ifEmpty { it.toString().uppercase() }} ") else println() }
    println()
    println("=====================================")
}