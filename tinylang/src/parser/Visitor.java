package parser;

public interface Visitor {
    void visit(ASTProgram program);
    void visit(ASTStatementNode statementNode);
    void visit(ASTExpressionNode expressionNode);
    void visit(ASTExpressionBinary expressionBinary);
    void visit(ASTExpressionFunctionCall expressionFunctionCall);
    void visit(ASTExpressionIdentifier expressionIdentifier);
    void visit(ASTExpressionLiteralBool expressionLiteralBool);
    void visit(ASTExpressionLiteralFloat expressionLiteralFloat);
    void visit(ASTExpressionLiteralInt expressionLiteralInt);
    void visit(ASTExpressionSubexpression expressionSubexpression);
    void visit(ASTExpressionUnary expressionUnary);
    void visit(ASTFormalParams formalParams);
    void visit(ASTStatementAssignment statementAssignment);
    void visit(ASTStatementBlock statementBlock);
    void visit(ASTStatementFor statementFor);
    void visit(ASTStatementFunctionDeclare statementFunctionDeclare);
    void visit(ASTStatementIf statementIf);
    void visit(ASTStatementPrint statementPrint);
    void visit(ASTStatementReturn statementReturn);
    void visit(ASTStatementVarDeclare statementVarDeclare);
}
