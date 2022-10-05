package parser;

import java.util.ArrayList;

public class ASTStatementIf extends ASTStatementNode{
    public ASTExpressionNode expression;
    public ASTStatementNode ifBlock;
    public ASTStatementNode elseBlock;

    public ASTStatementIf(ASTExpressionNode expression, ASTStatementNode ifBlock){
        this.expression = expression;
        this.ifBlock = ifBlock;
        this.elseBlock = null;
    }

    public ASTStatementIf(ASTExpressionNode expression, ASTStatementNode ifBlock, ASTStatementNode elseBlock){
        this.expression = expression;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
