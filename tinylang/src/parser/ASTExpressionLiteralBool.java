package parser;

public class ASTExpressionLiteralBool extends ASTExpressionNode{
    public boolean value;

    public ASTExpressionLiteralBool(boolean value){
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
