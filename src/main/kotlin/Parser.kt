class Parser(private val tokens: List<Token>) {
    private var current = 0

    fun parse(): List<Expr> {
        val expressions = mutableListOf<Expr>()
        while (!isAtEnd()) {
            try {
                expressions.add(declaration())
            } catch (e: ParseError) {
                println("Parse error at token: ${peek()}")
                println(e.message)
                synchronize()
            }
        }
        return expressions
    }

    private fun synchronize() {
        advance()
        while (!isAtEnd()) {
            if (previous() is Token.NewLine) return
            when (peek()) {
                is Token.Let, is Token.Var, is Token.Fun, is Token.If, is Token.When -> return
                else -> advance()
            }
        }
    }

    private fun declaration(): Expr {
        return when {
            match(Token.Let, Token.Var) -> variableDeclaration()
            match(Token.Fun) -> functionDeclaration()
            else -> statement()
        }
    }

    private fun variableDeclaration(): Expr {
        val name = consume(Token.Identifier::class.java, "Expect variable name.")
        var initializer: Expr? = null
        if (match(Token.Equal)) {
            initializer = expression()
        }
        consume(Token.NewLine::class.java, "Expect newline after variable declaration.")
        return Expr.Binary(Expr.Literal(name), Token.Equal, initializer ?: Expr.Literal(Token.Identifier("null")))
    }

    private fun functionDeclaration(): Expr {
        val name = consume(Token.Identifier::class.java, "Expect function name.")
        consume(Token.LParen::class.java, "Expect '(' after function name.")
        consume(Token.RParen::class.java, "Expect ')' after parameters.")
        consume(Token.LBrace::class.java, "Expect '{' before function body.")
        val body = block()
        return Expr.Binary(Expr.Literal(name), Token.Fun, body)
    }

    private fun statement(): Expr {
        return when {
            match(Token.If) -> ifStatement()
            match(Token.LBrace) -> Expr.Grouping(block())
            else -> expressionStatement()
        }
    }

    private fun ifStatement(): Expr {
        consume(Token.LParen::class.java, "Expect '(' after 'if'.")
        val condition = expression()
        consume(Token.RParen::class.java, "Expect ')' after if condition.")
        val thenBranch = statement()
        var elseBranch: Expr? = null
        if (match(Token.Else)) {
            elseBranch = statement()
        }
        return Expr.Binary(condition, Token.If, Expr.Binary(thenBranch, Token.Else, elseBranch ?: Expr.Literal(Token.Identifier("null"))))
    }

    private fun block(): Expr {
        val statements = mutableListOf<Expr>()
        while (!check(Token.RBrace) && !isAtEnd()) {
            statements.add(declaration())
        }
        consume(Token.RBrace::class.java, "Expect '}' after block.")
        return Expr.Grouping(Expr.Binary(Expr.Literal(Token.LBrace), Token.Comma, Expr.Literal(Token.RBrace)))
    }

    private fun expressionStatement(): Expr {
        val expr = expression()
        consume(Token.NewLine::class.java, "Expect newline after expression.")
        return expr
    }

    private fun expression(): Expr {
        return equality()
    }

    private fun equality(): Expr {
        var expr = comparison()
        while (match(Token.Equal)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun comparison(): Expr {
        var expr = term()
        while (match(Token.GreaterThan)) {
            val operator = previous()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun term(): Expr {
        var expr = factor()
        while (match(Token.Plus, Token.Minus)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun factor(): Expr {
        var expr = unary()
        while (match(Token.Multiply, Token.Divide)) {
            val operator = previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun unary(): Expr {
        if (match(Token.Minus)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }
        return primary()
    }

    private fun primary(): Expr {
        if (match(Token.IntegerLiteral::class.java, Token.FloatingLiteral::class.java, Token.StringLiteral::class.java)) {
            return Expr.Literal(previous())
        }
        if (match(Token.Identifier::class.java)) {
            return Expr.Literal(previous())
        }
        if (match(Token.LParen)) {
            val expr = expression()
            consume(Token.RParen::class.java, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }
        throw ParseError("Expect expression.")
    }

    private fun match(vararg types: Token): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun match(vararg types: Class<out Token>): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun consume(type: Class<out Token>, message: String): Token {
        if (check(type)) return advance()
        throw ParseError(message)
    }

    private fun check(type: Token): Boolean {
        if (isAtEnd()) return false
        return peek()::class == type::class
    }

    private fun check(type: Class<out Token>): Boolean {
        if (isAtEnd()) return false
        return type.isInstance(peek())
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean {
        return current >= tokens.size
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    class ParseError(message: String) : RuntimeException(message)
}