import lexer.Token;
import parser.ASTProgram;

import java.util.ArrayList;

public class main {
    public static void main(String[] args){
        Lexer lexer = new Lexer("programs/test1.txt");
        ArrayList<Token> lexedTokens = lexer.getAllTokens();

        for(Token token : lexedTokens){
            System.out.println(token.type);
        }

        Parser parser = new Parser(lexer);
        ASTProgram program = parser.parse();

        XMLGenerator xmlGenerator = new XMLGenerator();
        xmlGenerator.printToXML(program);

        Semantic semantic = new Semantic();
        semantic.visit(program);
    }
}
