import parser.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class XMLGenerator implements Visitor {
    private int indent = 0;
    PrintWriter writer;

    public void printToXML(ASTProgram program){
        try {
            writer = new PrintWriter("src/xml-output/output.xml", "UTF-8");

            program.accept(this);

            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error when creating output XML file. FileNotFound");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error when creating output XML file. UnsupportedEncodingException");
            e.printStackTrace();
        }

    }

    private String getIndents(){
        String indents = "";
        for(int i = 0; i < indent; i++){
            indents += "\t";
        }

        return indents;
    }

    @Override
    public void visit(ASTProgram node) {
        writer.println(getIndents() + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println(getIndents() + "<ProgramNode>");
        indent++;
        for(ASTStatementNode statement : node.statements){
            statement.accept(this);
        }
        indent--;
        writer.println(getIndents() + "</ProgramNode>");
    }

    @Override
    public void visit(ASTStatementNode node) {

    }

    @Override
    public void visit(ASTExpressionNode node) {

    }

    @Override
    public void visit(ASTExpressionBinary node) {
        writer.println(getIndents() + "<BinaryExpression Op='" + node.op + "'>");
        indent++;
        node.LHS.accept(this);
        node.RHS.accept(this);
        indent--;
        writer.println(getIndents() + "</BinaryExpression>");

    }

    @Override
    public void visit(ASTExpressionFunctionCall node) {
        writer.println(getIndents() + "<FunctionCall>");
        indent++;
        writer.println(getIndents() + "<Identifier>" + node.identifier + "</Identifier>");
        for(ASTExpressionNode actualParam : node.actualParams){
            actualParam.accept(this);
        }
        indent--;
        writer.println(getIndents() + "</FunctionCall>");
    }

    @Override
    public void visit(ASTExpressionIdentifier node) {
        writer.println(getIndents() + "<Identifier>" + node.identifier + "</Identifier>");
    }

    @Override
    public void visit(ASTExpressionLiteralBool node) {
        if(node.value){
            writer.println(getIndents() + "<BoolLiteral>true</BoolLiteral>");
        } else {
            writer.println(getIndents() + "<BoolLiteral>false</BoolLiteral>");
        }
    }

    @Override
    public void visit(ASTExpressionLiteralFloat node) {
        writer.println(getIndents() + "<FloatLiteral>" + node.value + "</FloatLiteral>");
    }

    @Override
    public void visit(ASTExpressionLiteralInt node) {
        writer.println(getIndents() + "<IntLiteral>" + node.value + "</IntLiteral>");
    }

    @Override
    public void visit(ASTExpressionSubexpression node) {
        node.expression.accept(this);
    }

    @Override
    public void visit(ASTExpressionUnary node) {
        writer.println(getIndents() + "<UnaryExpression Op='" + node.value + "'>");
        indent++;
        node.expression.accept(this);
        indent--;
        writer.println(getIndents() + "</UnaryExpression>");
    }

    @Override
    public void visit(ASTFormalParams node) {
        writer.println(getIndents() + "<FormalParam Type='" + node.type + "'>");
        indent++;
        writer.println(getIndents() + "<Identifier>" + node.identifier + "</Identifier>");
        indent--;
        writer.println(getIndents() + "</FormalParam>");
    }

    @Override
    public void visit(ASTStatementAssignment node) {
        writer.println(getIndents() + "<Assignment>");
        indent++;
        writer.println(getIndents() + "<Identifier>" + node.identifier + "</Identifier>");
        writer.println(getIndents() + "<Expression>");
        indent++;
        node.expression.accept(this);
        indent--;
        writer.println(getIndents() + "</Expression>");
        indent--;
        writer.println(getIndents() + "</Assignment>");
    }

    @Override
    public void visit(ASTStatementBlock node) {
        writer.println(getIndents() + "<BlockStatement>");
        indent++;
        for(ASTStatementNode statement : node.statements){
            statement.accept(this);
        }
        indent--;
        writer.println(getIndents() + "</BlockStatement>");
    }

    @Override
    public void visit(ASTStatementFor node) {
        writer.println(getIndents() + "<ForStatement>");
        indent++;
        if(node.varDeclare != null) {
            node.varDeclare.accept(this);
        }
        writer.println(getIndents() + "<Expression>");
        indent++;
        node.expression.accept(this);
        indent--;
        writer.println(getIndents() + "</Expression>");
        if(node.assignment != null){
            node.assignment.accept(this);
        }
        indent--;
        writer.println(getIndents() + "</ForStatement>");
    }

    @Override
    public void visit(ASTStatementFunctionDeclare node) {
        writer.println(getIndents() + "<FunctionDeclare Type='" + node.type + "'>");
        indent++;
        writer.println(getIndents() + "<Identifier>" + node.identifier + "</Identifier>");
        writer.println(getIndents() + "<FormalParams>");
        indent++;
        for(ASTFormalParams formalParam : node.formalParams){
            formalParam.accept(this);
        }
        indent--;
        writer.println(getIndents() + "</FormalParams>");
        node.block.accept(this);
        indent--;
        writer.println(getIndents() + "</FunctionDeclare>");
    }

    @Override
    public void visit(ASTStatementIf node) {
        writer.println(getIndents() + "<IfStatement>");
        indent++;
        writer.println(getIndents() + "<Expression>");
        indent++;
        node.expression.accept(this);
        indent--;
        writer.println(getIndents() + "</Expression>");
        node.ifBlock.accept(this);
        indent--;
        writer.println(getIndents() + "</IfStatement>");
        if(node.elseBlock != null){
            writer.println(getIndents() + "<ElseStatement>");
            indent++;
            node.elseBlock.accept(this);
            indent--;
            writer.println(getIndents() + "</ElseStatement>");
        }
    }

    @Override
    public void visit(ASTStatementPrint node) {
        writer.println(getIndents() + "<PrintStatement>");
        indent++;
        writer.println(getIndents() + "<Expression>");
        indent++;
        node.expression.accept(this);
        indent--;
        writer.println(getIndents() + "</Expression>");
        indent--;
        writer.println(getIndents() + "</PrintStatement>");
    }

    @Override
    public void visit(ASTStatementReturn node) {
        writer.println(getIndents() + "<ReturnStatement>");
        indent++;
        writer.println(getIndents() + "<Expression>");
        indent++;
        node.expression.accept(this);
        indent--;
        writer.println(getIndents() + "</Expression>");
        indent--;
        writer.println(getIndents() + "</ReturnStatement>");
    }

    @Override
    public void visit(ASTStatementVarDeclare node) {
        writer.println(getIndents() + "<VariableDeclare>");
        indent++;
        writer.println(getIndents() + "<Variable Type='" + node.type + "'>" + node.identifier + "</Variable>");
        writer.println(getIndents() + "<Expression>");
        indent++;
        node.expression.accept(this);
        indent--;
        writer.println(getIndents() + "</Expression>");
        indent--;
        writer.println(getIndents() + "</VariableDeclare>");
    }
}
