package parser;

import java.util.ArrayList;

public class ASTStatementFor extends ASTStatementNode{
    public ASTStatementNode varDeclare;
    public ASTExpressionNode expression;
    public ASTStatementNode assignment;
    public ASTStatementNode block;

    public ASTStatementFor(ASTStatementNode varDeclare, ASTExpressionNode expression, ASTStatementNode assignment, ASTStatementNode block){
        this.varDeclare = varDeclare;
        this.expression = expression;
        this.assignment = assignment;
        this.block = block;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
