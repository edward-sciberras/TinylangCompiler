package parser;

public class ASTExpressionIdentifier extends ASTExpressionNode{
    public String identifier;

    public ASTExpressionIdentifier(String identifier){
        this.identifier = identifier;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
