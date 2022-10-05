package lexer;

public class Token {
    public TokenType type;
    public String name;
    public float value;

    // constructor overloading
    public Token(TokenType type){
        this.type = type;
    }

    public Token(TokenType type, String name){
        this.type = type;
        this.name = name;
    }

    public Token(TokenType type, float value){
        this.type = type;
        this.value = value;
    }

    public Token(TokenType type, String name, float value){
        this.type = type;
        this.value = value;
        this.name = name;
    }
}
