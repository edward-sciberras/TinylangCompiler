package parser;

import java.util.ArrayList;

public class ASTStatementBlock extends ASTStatementNode{
    public ArrayList<ASTStatementNode> statements;

    public ASTStatementBlock(ArrayList<ASTStatementNode> statements){
        this.statements = statements;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
