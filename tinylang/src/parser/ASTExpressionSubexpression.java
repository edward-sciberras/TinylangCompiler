package parser;

public class ASTExpressionSubexpression extends ASTExpressionNode{
    public ASTExpressionNode expression;

    public ASTExpressionSubexpression(ASTExpressionNode expression){
        this.expression = expression;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
