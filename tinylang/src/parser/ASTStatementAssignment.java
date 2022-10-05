package parser;

public class ASTStatementAssignment extends ASTStatementNode{
    public String identifier;
    public ASTExpressionNode expression;

    public ASTStatementAssignment(String identifier, ASTExpressionNode expression){
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
