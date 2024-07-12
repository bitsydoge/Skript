sealed class Token {
    // Complex Token
    data class Identifier(val name: String) : Token()
    data class IntegerLiteral(val value: Int) : Token()
    data class FloatingLiteral(val value: Float) : Token()
    data class StringLiteral(val value: String) : Token()

    // Simple Token
    data object Plus : Token()
    data object Minus : Token()
    data object Multiply : Token()
    data object Divide : Token()
    data object Dot : Token()
    data object LParen : Token()
    data object RParen : Token()
    data object LBrace : Token()
    data object RBrace : Token()
    data object LBracket : Token()
    data object RBracket : Token()
    data object Let : Token()
    data object Var : Token()
    data object Fun : Token()
    data object If : Token()
    data object Else : Token()
    data object When : Token()
    data object Arrow : Token()
    data object Comma : Token()
    data object Equal : Token()
    data object GreaterThan : Token()
    data object NewLine : Token()

    fun toStringComplexToken(): String {
        return when (this) {
            is Identifier -> "IDENTIFIER($name)"
            is FloatingLiteral -> "FLOATINGLITERAL($value)"
            is IntegerLiteral -> "INTEGERLITERAL($value)"
            is StringLiteral -> "STRINGLITERAL(\"$value\")"
            else -> ""
        }
    }
}