sealed class Token(open val line: Int, open val column: Int) {
    private fun appendLineChar() : String {
        return "$line, $column"
    }

    fun toStringComplexToken(): String {
        val lineChar = appendLineChar()
        val str = when (this) {
            is Identifier -> "IDENTIFIER($name, $lineChar)"
            is FloatingLiteral -> "FLOATINGLITERAL($value, $lineChar)"
            is IntegerLiteral -> "INTEGERLITERAL($value, $lineChar)"
            is StringLiteral -> "STRINGLITERAL(\"$value\", $lineChar)"
            else -> (this::class.simpleName ?: "").uppercase() + "(" + line + ", " + column + ")"
        }
        return str
    }

    // Complex Token
    data class Identifier(val name: String, override val line: Int, override val column: Int) : Token(line, column)
    data class IntegerLiteral(val value: Int, override val line: Int, override val column: Int) : Token(line, column)
    data class FloatingLiteral(val value: Float, override val line: Int, override val column: Int) : Token(line, column)
    data class StringLiteral(val value: String, override val line: Int, override val column: Int) : Token(line, column)

    // Simple Token
    data class Plus(override val line: Int, override val column: Int) : Token(line, column)
    data class Minus(override val line: Int, override val column: Int) : Token(line, column)
    data class Multiply(override val line: Int, override val column: Int) : Token(line, column)
    data class Divide(override val line: Int, override val column: Int) : Token(line, column)
    data class Dot(override val line: Int, override val column: Int) : Token(line, column)
    data class LParen(override val line: Int, override val column: Int) : Token(line, column)
    data class RParen(override val line: Int, override val column: Int) : Token(line, column)
    data class LBrace(override val line: Int, override val column: Int) : Token(line, column)
    data class RBrace(override val line: Int, override val column: Int) : Token(line, column)
    data class LBracket(override val line: Int, override val column: Int) : Token(line, column)
    data class RBracket(override val line: Int, override val column: Int) : Token(line, column)
    data class Let(override val line: Int, override val column: Int) : Token(line, column)
    data class Var(override val line: Int, override val column: Int) : Token(line, column)
    data class Fun(override val line: Int, override val column: Int) : Token(line, column)
    data class If(override val line: Int, override val column: Int) : Token(line, column)
    data class Else(override val line: Int, override val column: Int) : Token(line, column)
    data class When(override val line: Int, override val column: Int) : Token(line, column)
    data class Arrow(override val line: Int, override val column: Int) : Token(line, column)
    data class Comma(override val line: Int, override val column: Int) : Token(line, column)
    data class Equal(override val line: Int, override val column: Int) : Token(line, column)
    data class GreaterThan(override val line: Int, override val column: Int) : Token(line, column)
    data class GreaterOrEqualThan(override val line: Int, override val column: Int) : Token(line, column)
    data class LowerThan(override val line: Int, override val column: Int) : Token(line, column)
    data class LowerOrEqualThan(override val line: Int, override val column: Int) : Token(line, column)
    data class NewLine(override val line: Int, override val column: Int) : Token(line, column)
}