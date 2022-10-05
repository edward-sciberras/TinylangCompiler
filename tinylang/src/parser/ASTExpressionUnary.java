package parser;

public class ASTExpressionUnary extends ASTExpressionNode{
    public String value;
    public ASTExpressionNode expression;

    public ASTExpressionUnary(String value, ASTExpressionNode expression){
        this.expression = expression;
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
