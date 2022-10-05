package lexer;

public enum TokenType {
    TOK_EOF,
    TOK_Error,
    TOK_LeftBracket,
    TOK_RightBracket,
    TOK_LeftCurly,
    TOK_RightCurly,
    TOK_Multiplication,
    TOK_Addition,
    TOK_Relation,
    TOK_Comma,
    TOK_Colon,
    TOK_Semicolon,
    TOK_Equal,
    TOK_BooleanValue,
    TOK_FloatValue,
    TOK_IntegerValue,
    TOK_BooleanKeyword,
    TOK_FloatKeyword,
    TOK_IntegerKeyWord,
    TOK_Identifier,
    TOK_Let,
    TOK_Print,
    TOK_Return,
    TOK_Function,
    TOK_If,
    TOK_Else,
    TOK_For,
    TOK_Logic,
    TOK_Comment
}