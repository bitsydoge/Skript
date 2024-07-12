sealed class Expr {
    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr()
    data class Unary(val operator: Token, val expr: Expr) : Expr()
    data class Literal(val value: Token) : Expr()
    data class Grouping(val expression: Expr) : Expr()
    data class Variable(val name: Token) : Expr()
    data class Assign(val name: Token, val value: Expr) : Expr()
    data class VariableDecl(val name: Token, val initializer: Expr?) : Expr()
    data class FunctionDecl(val name: Token, val params: List<Token>, val body: List<Expr>) : Expr()
    data class If(val condition: Expr, val thenBranch: Expr, val elseBranch: Expr?) : Expr()
    data class Call(val callee: Expr, val arguments: List<Expr>) : Expr()
    data class Block(val statements: List<Expr>) : Expr()
}