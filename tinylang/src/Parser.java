import lexer.*;
import parser.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Parser {
    public Lexer lexer;
    public Token currToken = new Token(TokenType.TOK_EOF);

    public Parser(Lexer lexer){
        this.lexer = lexer;
    }

    public ASTProgram parse(){
        Lexer.charIndex = 0;
        ArrayList<ASTStatementNode> statements = new ArrayList<>();
        currToken = lexer.getNextToken();

        while(currToken.type != TokenType.TOK_EOF){
            ASTStatementNode currStatement = getStatement();
            currToken = lexer.getNextToken();
            statements.add(currStatement);
        }

        return new ASTProgram(statements);
    }

    private ASTStatementNode getStatement(){
        switch (currToken.type){
            case TOK_Let:
                ASTStatementNode variableDeclaration = parseVariableDeclaration();

                currToken = lexer.getNextToken();
                if(currToken.type != TokenType.TOK_Semicolon){
                    System.out.println("';' expected after equal sign in variable declaration");
                    System.exit(1);
                }

                return variableDeclaration;

            case TOK_Identifier:
                ASTStatementNode assignmentNode = parseAssignment();

                currToken = lexer.getNextToken();
                if(currToken.type != TokenType.TOK_Semicolon){
                    System.out.println("';' expected after expression in assignment statement.");
                    System.exit(1);
                }

                return assignmentNode;

            case TOK_Print:
                ASTStatementNode printNode = parsePrintStatement();

                currToken = lexer.getNextToken();
                if(currToken.type != TokenType.TOK_Semicolon){
                    System.out.println("';' expected after expression in print statement.");
                    System.exit(1);
                }

                return printNode;

            case TOK_If:
                return parseIfStatement();
            case TOK_LeftCurly:
                return parseBlock();
            case TOK_For:
                return parseForStatement();
            case TOK_Return:
                ASTExpressionNode expressionNode = getExpression();
                if(lexer.checkNextToken() != TokenType.TOK_Semicolon){
                    System.out.println("Semicolon expected after an expression in the return statement.");
                    System.exit(1);
                }

                currToken = lexer.getNextToken();
                return new ASTStatementReturn(expressionNode);
            case TOK_Function:
                return parseFunctionDeclare();
            default:
                System.out.println("Given statement token not revoginised");
                System.exit(1);
        }

        return null;
    }

    private ASTStatementNode parseFunctionDeclare(){
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Identifier){
            System.out.println("Identifier expected after 'fn' statement in function declaration.");
            System.exit(1);
        }

        String identifier = currToken.name;
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_LeftBracket){
            System.out.println("Left bracket expected after Identifier statement in function declaration.");
            System.exit(1);
        }

        ArrayList<ASTFormalParams> formalParams = new ArrayList<>();
        while(currToken.type != TokenType.TOK_RightBracket){
            formalParams.add(getFormalParams());
            currToken = lexer.getNextToken();

            if(currToken.type != TokenType.TOK_RightBracket && currToken.type != TokenType.TOK_Comma){
                System.out.println("Comma or right bracket expected in list of params in function declaration.");
                System.exit(1);
            }
        }

        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Addition){
            System.out.println("'-' expected after params in function declaration.");
            System.exit(1);
        }

        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Relation){
            System.out.println("'>' expected after '- in function declaration.");
            System.exit(1);
        }

        currToken = lexer.getNextToken();
        TokenType currTokenType = currToken.type;
        if((currTokenType != TokenType.TOK_IntegerKeyWord) && (currTokenType != TokenType.TOK_FloatKeyword) && (currTokenType != TokenType.TOK_BooleanKeyword)){
            System.out.println("Function return type expected. ");
            System.exit(1);
        }

        String tokenType = lexer.tokenToString(currToken.type);
        ASTStatementNode blockStatement = parseBlock();

        return new ASTStatementFunctionDeclare(identifier, formalParams, tokenType, blockStatement);
    }

    private ASTFormalParams getFormalParams(){
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Identifier){
            System.out.println("Identifier is expected in formal params.");
            System.exit(1);
        }

        String identifier = currToken.name;

        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Colon){
            System.out.println("Colon is expected in formal params.");
            System.exit(1);
        }

        currToken = lexer.getNextToken();
        TokenType currTokenType = currToken.type;
        if((currTokenType != TokenType.TOK_IntegerKeyWord) && (currTokenType != TokenType.TOK_FloatKeyword) && (currTokenType != TokenType.TOK_BooleanKeyword)){
            System.out.println("Variable type expected after Colon in variable declaration");
            System.exit(1);
        }

        return new ASTFormalParams(identifier, lexer.tokenToString(currToken.type));
    }

    private ASTStatementNode parseForStatement(){
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_LeftBracket){
            System.out.println("Left bracket expected after 'for' in for statement");
            System.exit(1);
        }

        ASTStatementNode varDeclare = null;
        if(lexer.checkNextToken() == TokenType.TOK_Let){
            currToken = lexer.getNextToken();
            varDeclare = parseVariableDeclaration();
        }

        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Semicolon){
            System.out.println("Semicolon expected after variable declaration in for statement");
            System.exit(1);
        }

        ASTExpressionNode expressionNode = getExpression();
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Semicolon){
            System.out.println("Semicolon expected after expression in for statement");
            System.exit(1);
        }

        ASTStatementNode assignment = null;
        if(lexer.checkNextToken() == TokenType.TOK_Identifier){
            currToken = lexer.getNextToken();
            assignment = parseAssignment();
        }

        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_RightBracket){
            System.out.println("Right bracket expected after assignment in for statement");
            System.exit(1);
        }

        ASTStatementNode blockStatement = parseBlock();
        return new ASTStatementFor(varDeclare, expressionNode, assignment, blockStatement);
    }

    private ASTStatementNode parseIfStatement(){
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_LeftBracket){
            System.out.println("Left bracket expected after 'if' in if statement");
            System.exit(1);
        }

        ASTExpressionNode expressionNode = getExpression();

        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_RightBracket){
            System.out.println("Right bracket expected after 'if' in if statement");
            System.exit(1);
        }

        ASTStatementNode ifBlock = parseBlock();
        if(lexer.checkNextToken() == TokenType.TOK_Else){
            currToken = lexer.getNextToken();
            ASTStatementNode elseBlock = parseBlock();
            return new ASTStatementIf(expressionNode, ifBlock, elseBlock);
        }
        return new ASTStatementIf(expressionNode, ifBlock);
    }

    private ASTStatementNode parsePrintStatement(){
        ASTExpressionNode expressionNode = getExpression();
        return new ASTStatementPrint(expressionNode);
    }

    private ASTStatementNode parseAssignment(){
        String identifier = currToken.name;

        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Equal){
            System.out.println("Equals expected after identifier in assignment.");
            System.exit(1);
        }

        ASTExpressionNode expressionNode = getExpression();
        return new ASTStatementAssignment(identifier, expressionNode);
    }

    private ASTStatementNode parseVariableDeclaration(){
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Identifier){
            System.out.println("Identifier expected after 'let' in variable declaration");
            System.exit(1);
        }

        String identifier = currToken.name;
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Colon){
            System.out.println("Colon expected after Identifier in variable declaration");
            System.exit(1);
        }

        currToken = lexer.getNextToken();
        TokenType currTokenType = currToken.type;
        if((currTokenType != TokenType.TOK_IntegerKeyWord) && (currTokenType != TokenType.TOK_FloatKeyword) && (currTokenType != TokenType.TOK_BooleanKeyword)){
            System.out.println("Variable type expected after Colon in variable declaration");
            System.exit(1);
        }

        String tokenType = lexer.tokenToString(currToken.type);
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_Equal){
            System.out.println("Equals expected after Variable in variable declaration");
            System.exit(1);
        }

        ASTExpressionNode expressionNode = getExpression();
        return new ASTStatementVarDeclare(identifier, tokenType, expressionNode);
    }

    private ASTExpressionNode getExpression(){
        ASTExpressionNode simpleExpression = parseSimpleExpression();

        if(lexer.checkNextToken() != TokenType.TOK_Relation){
            return simpleExpression;
        }

        String operation = lexer.getNextToken().name;
        return new ASTExpressionBinary(simpleExpression, getExpression(), operation);
    }

    private ASTExpressionNode parseSimpleExpression(){
        ASTExpressionNode term = parseTerm();

        if(lexer.checkNextToken() != TokenType.TOK_Addition){
            return term;
        }

        String operation = lexer.getNextToken().name;
        return new ASTExpressionBinary(term, getExpression(), operation);
    }

    private ASTStatementNode parseBlock(){
        currToken = lexer.getNextToken();
        if(currToken.type != TokenType.TOK_LeftCurly){
            System.out.println("Left curly bracket expected for the block statement");
            System.exit(1);
        }

        currToken = lexer.getNextToken();
        ArrayList<ASTStatementNode> statements = new ArrayList<>();
        while(currToken.type != TokenType.TOK_RightCurly){
            statements.add(getStatement());
            currToken = lexer.getNextToken();
        }

        return new ASTStatementBlock(statements);
    }

    private ASTExpressionNode parseTerm(){
        ASTExpressionNode factor = parseFactor();

        if(lexer.checkNextToken() != TokenType.TOK_Multiplication){
            return factor;
        }

        String operation = lexer.getNextToken().name;
        return new ASTExpressionBinary(factor, getExpression(), operation);
    }

    private ASTExpressionNode parseFactor(){
        currToken = lexer.getNextToken();

        switch (currToken.type){
            case TOK_BooleanValue:
                if(currToken.name.equals("true")){
                    return new ASTExpressionLiteralBool(true);
                } else {
                    return new ASTExpressionLiteralBool(false);
                }

            case TOK_IntegerValue:
                DecimalFormat df = new DecimalFormat("0");
                return new ASTExpressionLiteralInt(Integer.parseInt(df.format(currToken.value)));

            case TOK_FloatValue:
                return new ASTExpressionLiteralFloat(currToken.value);

            case TOK_Identifier:
                String identifier = currToken.name;
                if(lexer.checkNextToken() == TokenType.TOK_LeftBracket){
                    currToken = lexer.getNextToken();
                    ArrayList<ASTExpressionNode> expressions = new ArrayList<>();

                    while(lexer.checkNextToken() != TokenType.TOK_RightBracket){
                        ASTExpressionNode expression = getExpression();
                        expressions.add(expression);

                        if(lexer.checkNextToken() == TokenType.TOK_Comma){
                            currToken = lexer.getNextToken();
                        }
                    }

                    currToken = lexer.getNextToken();
                    return new ASTExpressionFunctionCall(identifier, expressions);
                }
                return new ASTExpressionIdentifier(identifier);

            case TOK_LeftBracket:
                ASTExpressionNode expression = getExpression();
                currToken = lexer.getNextToken();

                if(currToken.type != TokenType.TOK_RightBracket){
                    System.out.println("Right bracket expected in this subexpression.");
                    System.exit(1);
                }
                return new ASTExpressionSubexpression(expression);

            case TOK_Addition:
            case TOK_Logic:
                if(currToken.name.equals("-") || currToken.name.equals("not")){
                    System.out.println("'not' or '-' expected in unary expression.");
                    System.exit(1);
                }

                return new ASTExpressionUnary(currToken.name, getExpression());
            default:
                System.out.println("Unknown factor reached.");
                System.exit(1);
        }
        return null;
    }
}