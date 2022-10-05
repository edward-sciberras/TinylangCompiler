package parser;

public class ASTStatementNode implements ASTNode{
    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
