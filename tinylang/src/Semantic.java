import parser.*;
import semantic.Symbol;
import semantic.SymbolTable;

import java.util.ArrayList;

public class Semantic implements Visitor {
    public ArrayList<SymbolTable> symbolTables = new ArrayList<>();
    public String currLiteralType = "";
    public boolean binaryCheck = false;
    public String literalType = "";

    // adds new table to list
    private void addTable(){
        symbolTables.add(new SymbolTable());
    }

    // gets last table in list
    private SymbolTable getLastTable(){
        return symbolTables.get(symbolTables.size() - 1);
    }

    // removes last table in list
    private void removeLastTable(){
        symbolTables.remove(symbolTables.size() - 1);
    }

    // checks if identifier is in all tables
    private Symbol checkTables(String identifier){
        for(int i = (symbolTables.size() - 1); i >= 0; i--){
            if(symbolTables.get(i).search(identifier) != null){
                return symbolTables.get(i).search(identifier);
            }
        }
        return null;
    }

    @Override
    public void visit(ASTProgram node) {
        // adds new symbol table for new scope
        addTable();
        for(ASTStatementNode statement : node.statements){
            statement.accept(this);
        }
        removeLastTable();
    }

    @Override
    public void visit(ASTStatementNode node) {

    }

    @Override
    public void visit(ASTExpressionNode node) {

    }

    @Override
    public void visit(ASTExpressionBinary node) {
        if(currLiteralType.equals("") && symbolTables.size() > 1){
            binaryCheck = true;
            node.LHS.accept(this);
            node.RHS.accept(this);
            literalType = "";
            binaryCheck = false;
        } else {
            node.LHS.accept(this);
            node.RHS.accept(this);
        }
    }

    @Override
    public void visit(ASTExpressionFunctionCall node) {
        if(checkTables(node.identifier) == null){
            System.out.println("Function '" + node.identifier + "' was not declared and called.");
        } else {
            Symbol funcDeclare = checkTables(node.identifier);

            // checking if length and type of params is the same
            if(node.actualParams.size() != funcDeclare.functionParamTypes.size()){
                System.out.println("Function '" + node.identifier + "' has different parameters when declared and called.");
            }
        }
    }

    @Override
    public void visit(ASTExpressionIdentifier node) {
        // checking if already declared and that type is the same
        if(checkTables(node.identifier) == null){
            System.out.println("Variable '" + node.identifier + "' was not declared.");
        } else {
            if(binaryCheck && literalType.equals("")){
                literalType = checkTables(node.identifier).literalType;
            } else if (binaryCheck){
                if(!(literalType.equals(checkTables(node.identifier).literalType))){
                    System.out.println("Type mismatch with " + node.identifier);
                }
            } else {
                if(!(literalType.equals(checkTables(node.identifier).literalType))){
                    System.out.println("Type mismatch with " + node.identifier);
                }
            }
        }
    }

    @Override
    public void visit(ASTExpressionLiteralBool node) {
        if(binaryCheck && literalType.equals("")){
            literalType = "TOK_BooleanKeyword";
        } else if (binaryCheck){
            if(!literalType.equals("TOK_BooleanKeyword")){
                System.out.println("Type mismatch with " + node.value);
            }
        } else {
            if(!literalType.equals("TOK_BooleanKeyword")){
                System.out.println("Type mismatch with " + node.value);
            }
        }
    }

    @Override
    public void visit(ASTExpressionLiteralFloat node) {
        if(binaryCheck && literalType.equals("")){
            literalType = "TOK_FloatKeyword";
        } else if (binaryCheck){
            if(!literalType.equals("TOK_FloatKeyword")){
                System.out.println("Type mismatch with " + node.value);
            }
        } else {
            if(!literalType.equals("TOK_FloatKeyword")){
                System.out.println("Type mismatch with " + node.value);
            }
        }
    }

    @Override
    public void visit(ASTExpressionLiteralInt node) {
        if(binaryCheck && literalType.equals("")){
            literalType = "TOK_IntKeyword";
        } else if (binaryCheck){
            if(!literalType.equals("TOK_IntKeyword")){
                System.out.println("Type mismatch with " + node.value);
            }
        } else {
            if(!literalType.equals("TOK_IntKeyword")){
                System.out.println("Type mismatch with " + node.value);
            }
        }
    }

    @Override
    public void visit(ASTExpressionSubexpression node) {
        node.expression.accept(this);
    }

    @Override
    public void visit(ASTExpressionUnary node) {
        node.expression.accept(this);
    }

    @Override
    public void visit(ASTFormalParams node) {
        // declaring all parameters in the current scope
        SymbolTable currScope = getLastTable();
        currScope.insert(node.identifier, "variable", node.type);
    }

    @Override
    public void visit(ASTStatementAssignment node) {
        // checking if assignment already exists
        if(checkTables(node.identifier) != null){
            // checking if types match or not
            String prevLiteralType = currLiteralType;
            currLiteralType = checkTables(node.identifier).literalType;
            node.expression.accept(this);
            currLiteralType = prevLiteralType;
        } else {
            System.out.println("Variable '" + node.identifier + "' was not declared.");
        }
    }

    @Override
    public void visit(ASTStatementBlock node) {
        for(ASTStatementNode statement : node.statements){
            statement.accept(this);
        }
    }

    @Override
    public void visit(ASTStatementFor node) {
        // creating a new scope for the loop
        addTable();
        SymbolTable currScope = getLastTable();

        // if variable declaration is present, then insert into current scope
        if(node.varDeclare != null){
            node.varDeclare.accept(this);
        }

        // checking if expression is semantically valid
        String prevLiteralType = currLiteralType;
        currLiteralType = "";
        node.expression.accept(this);
        currLiteralType = prevLiteralType;

        // checking if assignment is valid
        if(node.assignment != null){
            node.assignment.accept(this);
        }

        node.block.accept(this);
        removeLastTable(); // removing scope
    }

    @Override
    public void visit(ASTStatementFunctionDeclare node) {
        // function declarations are all global and none are nested
        if(symbolTables.size() > 1){
            System.out.println("Function '" + node.identifier + "' is not global");
        } else {
            if(checkTables(node.identifier) == null){
                String prevLiteralType = currLiteralType;
                currLiteralType = node.type;

                // adding function to current scope
                SymbolTable currScope = getLastTable();
                currScope.insert(node.identifier, "function", node.type);

                // creating a new scope for the function block
                addTable();
                currScope = getLastTable();

                // adding function parameters to the new scope
                for(ASTFormalParams param : node.formalParams){
                    param.accept(this);
                    checkTables(node.identifier).functionParamTypes.add(param.type);
                }

                node.block.accept(this);
                // removing scope
                removeLastTable();
                currLiteralType = prevLiteralType;
            } else {
                System.out.println("Function '" + node.identifier + "' was already declared.");

            }
        }
    }

    @Override
    public void visit(ASTStatementIf node) {
        String prevLiteralType = currLiteralType;
        currLiteralType = "";
        node.expression.accept(this);
        currLiteralType = prevLiteralType;
        addTable();
        node.ifBlock.accept(this);
        removeLastTable();

        // checking if else block exists
        if(node.elseBlock != null){
            addTable();
            node.elseBlock.accept(this);
            removeLastTable();
        }
    }

    @Override
    public void visit(ASTStatementPrint node) {
        String prevLiteralType = currLiteralType;
        currLiteralType = "";
        node.expression.accept(this);
        currLiteralType = prevLiteralType;
    }

    @Override
    public void visit(ASTStatementReturn node) {
        // just checks expression
        node.expression.accept(this);
    }

    @Override
    public void visit(ASTStatementVarDeclare node) {
        // checking if node was previously declared
        if(checkTables(node.identifier) == null){
            // adding variable declaration to symbol table
            SymbolTable currScope = getLastTable();
            currScope.insert(node.identifier, "variable", node.type);
            String prevLiteralType = currLiteralType;
            currLiteralType = node.type;
            node.expression.accept(this);
            currLiteralType = prevLiteralType;
        } else {
            System.out.println("Variable '" + node.identifier + "' already declared in scope.");
        }
    }
}
