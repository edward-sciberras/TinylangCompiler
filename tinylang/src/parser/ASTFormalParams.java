package parser;

public class ASTFormalParams extends ASTExpressionNode{
    public String identifier;
    public String type;

    public ASTFormalParams(String identifier, String type){
        this.identifier = identifier;
        this.type = type;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
