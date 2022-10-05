package parser;

public class ASTStatementReturn extends ASTStatementNode{
    public ASTExpressionNode expression;

    public ASTStatementReturn(ASTExpressionNode expression){
        this.expression = expression;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
