package parser;

import java.util.ArrayList;

public class ASTProgram implements ASTNode{
    public ArrayList<ASTStatementNode> statements;

    public ASTProgram(ArrayList<ASTStatementNode> statements){
        this.statements = statements;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
