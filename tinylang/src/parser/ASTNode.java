package parser;

public interface ASTNode {
    public void accept(Visitor visitor);
}
