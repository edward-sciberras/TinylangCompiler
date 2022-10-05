package parser;

public class ASTStatementPrint extends ASTStatementNode{
    public ASTExpressionNode expression;

    public ASTStatementPrint(ASTExpressionNode expression){
        this.expression = expression;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
