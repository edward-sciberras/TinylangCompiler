package parser;

public class ASTExpressionLiteralInt extends ASTExpressionNode{
    public int value;

    public ASTExpressionLiteralInt(int value){
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
