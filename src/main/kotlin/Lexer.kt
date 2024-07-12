typealias TokenList = List<Token>

class Lexer(private val input: String) {
    private var position: Int = 0
    private val tokens = mutableListOf<Token>()

    fun tokenize(): TokenList {
        while (position < input.length) {
            when (val char = input[position]) {
                '+' -> addTokenAndAdvance(Token.Plus)
                '-' -> if (peekNext() == '>') {
                    position++
                    addTokenAndAdvance(Token.Arrow)
                } else {
                    addTokenAndAdvance(Token.Minus)
                }
                '*' -> addTokenAndAdvance(Token.Multiply)
                '/' -> addTokenAndAdvance(Token.Divide)
                '(' -> addTokenAndAdvance(Token.LParen)
                ')' -> addTokenAndAdvance(Token.RParen)
                '{' -> addTokenAndAdvance(Token.LBrace)
                '}' -> addTokenAndAdvance(Token.RBrace)
                '[' -> addTokenAndAdvance(Token.LBracket)
                ']' -> addTokenAndAdvance(Token.RBracket)
                ',' -> addTokenAndAdvance(Token.Comma)
                '=' -> addTokenAndAdvance(Token.Equal)
                '>' -> addTokenAndAdvance(Token.GreaterThan)
                '.' -> addTokenAndAdvance(Token.Dot)
                '\n' -> addTokenAndAdvance(Token.NewLine)
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
    }

    private fun peekNext(): Char? {
        return if (position + 1 < input.length) input[position + 1] else null
    }

    private fun tokenizeStringLiteral() {
        val start = ++position // Skip the opening quote
        while (position < input.length && input[position] != '"') {
            position++
        }
        if (position >= input.length) {
            throw IllegalArgumentException("Unterminated string literal")
        }
        val value = input.substring(start, position)
        position++ // Skip the closing quote
        tokens.add(Token.StringLiteral(value))
    }

    private fun tokenizeNumber() {
        val start = position
        while (position < input.length && input[position].isDigit()) {
            position++
        }
        if (position < input.length && input[position] == '.') {
            position++
            while (position < input.length && input[position].isDigit()) {
                position++
            }
            tokens.add(Token.FloatingLiteral(input.substring(start, position).toFloat()))
        } else {
            tokens.add(Token.IntegerLiteral(input.substring(start, position).toInt()))
        }
    }

    private fun tokenizeIdentifierOrKeyword() {
        val start = position
        while (position < input.length && input[position].isLetterOrDigit()) {
            position++
        }
        val token = when (val value = input.substring(start, position)) {
            "let" -> Token.Let
            "var" -> Token.Var
            "fun" -> Token.Fun
            "if" -> Token.If
            "else" -> Token.Else
            "when" -> Token.When
            else -> Token.Identifier(value)
        }
        tokens.add(token)
    }
}

fun TokenList.print() {
    println("=====================================")
    forEach { if(it != Token.NewLine) print("${it.toStringComplexToken().ifEmpty { it.toString().uppercase() }} ") else println() }
    println()
    println("=====================================")
}