package parser;

public class ASTExpressionBinary extends ASTExpressionNode {
    public ASTExpressionNode LHS;
    public ASTExpressionNode RHS;
    public String op;

    public ASTExpressionBinary(ASTExpressionNode LHS, ASTExpressionNode RHS, String op){
        this.LHS = LHS;
        this.RHS = RHS;
        this.op = op;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
