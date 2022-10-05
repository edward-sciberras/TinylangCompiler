package parser;

public class ASTExpressionLiteralFloat extends ASTExpressionNode{
    public float value;

    public ASTExpressionLiteralFloat(float value){
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
