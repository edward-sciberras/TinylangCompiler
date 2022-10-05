package parser;

import java.util.ArrayList;

public class ASTStatementFunctionDeclare extends ASTStatementNode{
    public String identifier;
    public ArrayList<ASTFormalParams> formalParams;
    public String type;
    public ASTStatementNode block;

    public ASTStatementFunctionDeclare(String identifier, ArrayList<ASTFormalParams> formalParams, String type, ASTStatementNode block){
        this.identifier = identifier;
        this.formalParams= formalParams;
        this.type = type;
        this.block = block;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
