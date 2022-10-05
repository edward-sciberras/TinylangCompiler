package parser;

public class ASTStatementVarDeclare extends ASTStatementNode{
    public String identifier;
    public String type;
    public ASTExpressionNode expression;

    public ASTStatementVarDeclare(String identifier, String type, ASTExpressionNode expression){
        this.identifier = identifier;
        this.type = type;
        this.expression = expression;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
