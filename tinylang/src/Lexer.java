import lexer.Classifier;
import lexer.State;
import lexer.Token;
import lexer.TokenType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Stream;

public class Lexer {
    public static int charIndex = 0;
    private static int lineNumber = 1;
    private String inputProgram = "";

    public static ArrayList<Token> lexedTokens = new ArrayList<>();

    public Lexer(String fileName){
        this.inputProgram = fileToString(fileName);
    }
    private static final State[] finalStates = {State.S01, State.S02, State.S03, State.S04, State.S06, State.S07, State.S08,
                                            State.S10, State.S11, State.S12, State.S13, State.S15, State.S18};

    State[][] transitionTable =
       {{State.S01,  State.S02,  State.S04,  State.S05,  State.S06,  State.S07,  State.S08,  State.S00,  State.S11,  State.S12,  State.S13,  State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.S03,  State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.S03,  State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.S03,  State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.S16,  State.S09,  State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.S09,  State.S09,  State.S09,  State.S09,  State.S09,  State.S09,  State.S10,  State.S09,  State.S09,  State.S09,  State.S09,  State.S09,  State.S09},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.S12,  State.S12,  State.SERR, State.S12,  State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.S13,  State.S14,  State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.S15,  State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.S15,  State.SERR, State.SERR, State.SERR},
        {State.SERR, State.S16,  State.S16,  State.S16,  State.S16,  State.S17,  State.S16,  State.S16,  State.S16,  State.S16,  State.S16,  State.S16,  State.S16,  State.S16},
        {State.SERR, State.S16,  State.S16,  State.S16,  State.S16,  State.S16,  State.S18,  State.S16,  State.S16,  State.S16,  State.S16,  State.S16,  State.S16,  State.S16},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR},
        {State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR, State.SERR}};
      //     EOF         <>           =           !           +           *           /           \n       ;:(){}      A-Za-z        0-9          .           _         other

    public ArrayList<Token> getAllTokens(){
        Token nextToken = new Token(TokenType.TOK_Error);
        while(!tokenToString(nextToken.type).equals("TOK_EOF")){
            nextToken = getNextToken();
            lexedTokens.add(nextToken);
        }

        return lexedTokens;
    }

    public TokenType checkNextToken(){
        int prevCharIndex = charIndex;
        TokenType type = getNextToken().type;
        charIndex = prevCharIndex;

        return type;
    }

    public Token getNextToken(){
        State currentState = State.S00;
        String lexeme = "";
        char thisChar;

        Stack<State> stack = new Stack<>();
        stack.push(State.SERR);

        while(currentState != State.SERR) {
            thisChar = inputProgram.charAt(charIndex);
            charIndex++;

            // clearing new lines and tabs to get to actual code
            while (lexeme.length() == 0 && (thisChar == '\n' || thisChar == '\t' || thisChar == ' ')) {
                if (thisChar == '\n') {
                    lineNumber++;
                }

                // if last character has been outputted then add EOF token and exit
                if(charIndex == inputProgram.length()){
                    return new Token(TokenType.TOK_EOF);
                }

                thisChar = inputProgram.charAt(charIndex);
                charIndex++;
            }

            lexeme += thisChar;
            if (isFinalState(currentState)) {
                stack.empty();
            }
            stack.push(currentState);
            currentState = transitionTable[stateToInt(currentState)][findClassifier(thisChar)];
        }

        // rollback loop
        while(!isFinalState(currentState) && currentState == State.SERR){
            currentState = stack.peek();
            stack.pop();
            lexeme = lexeme.substring(0, lexeme.length() - 1);
            charIndex--;
        }

        if(isFinalState(currentState)){
            Token token = getTokenType(currentState, lexeme);
            // ignoring comments for lexing and parsing
            if(token.type == TokenType.TOK_Comment){
                return getNextToken();
            }
            return token;
        } else {
            System.out.println("Error at line: " + lineNumber);
            System.exit(1);
        }
        return null;
    }

    // getting token type from state and lexeme
    private Token getTokenType(State state, String lexeme){
        if(state == State.S01)
            return new Token(TokenType.TOK_EOF);
        if(state == State.S02 || state == State.S03)
            return new Token(TokenType.TOK_Relation, lexeme);
        if(state == State.S04)
            return new Token(TokenType.TOK_Equal);
        if(state == State.S06)
            return new Token(TokenType.TOK_Addition, lexeme);
        if(state == State.S07 || state == State.S08)
            return new Token(TokenType.TOK_Multiplication, lexeme);
        if(state == State.S10){
            lineNumber++;
            return new Token(TokenType.TOK_Comment);
        }
        if(state == State.S18)
            return new Token(TokenType.TOK_Comment);
        if(state == State.S11)
            return checkPunctuation(lexeme);
        if(state == State.S12)
            return checkLetters(lexeme);
        if(state == State.S13)
            return new Token(TokenType.TOK_IntegerValue, Float.parseFloat(lexeme));
        if(state == State.S15)
            return new Token(TokenType.TOK_FloatValue, Float.parseFloat(lexeme));
        return new Token(TokenType.TOK_Error);
    }

    private Token checkPunctuation(String lexeme){
        if(lexeme.equals("}"))
            return new Token(TokenType.TOK_RightCurly);
        if(lexeme.equals("{"))
            return new Token(TokenType.TOK_LeftCurly);
        if(lexeme.equals("("))
            return new Token(TokenType.TOK_LeftBracket);
        if(lexeme.equals(")"))
            return new Token(TokenType.TOK_RightBracket);
        if(lexeme.equals(":"))
            return new Token(TokenType.TOK_Colon);
        if(lexeme.equals(";"))
            return new Token(TokenType.TOK_Semicolon);

        return new Token(TokenType.TOK_Comma);
    }

    // checking strings to see if they are keywords or identifiers
    private Token checkLetters(String lexeme){
        if(lexeme.equals("or") || lexeme.equals("and") || lexeme.equals("not")){
            return new Token(TokenType.TOK_Logic, lexeme);
        }
        if(lexeme.equals("true") || lexeme.equals("false")){
            return new Token(TokenType.TOK_BooleanValue, lexeme);
        }
        if(lexeme.equals("int")){
            return new Token(TokenType.TOK_IntegerKeyWord);
        }
        if(lexeme.equals("float")){
            return new Token(TokenType.TOK_FloatKeyword);
        }
        if(lexeme.equals("bool")){
            return new Token(TokenType.TOK_BooleanKeyword);
        }
        if(lexeme.equals("let")){
            return new Token(TokenType.TOK_Let);
        }
        if(lexeme.equals("return")){
            return new Token(TokenType.TOK_Return);
        }
        if(lexeme.equals("print")){
            return new Token(TokenType.TOK_Print);
        }
        if(lexeme.equals("if")){
            return new Token(TokenType.TOK_If);
        }
        if(lexeme.equals("else")){
            return new Token(TokenType.TOK_Else);
        }
        if(lexeme.equals("for")){
            return new Token(TokenType.TOK_For);
        }
        if(lexeme.equals("fn")){
            return new Token(TokenType.TOK_Function);
        }

        // if it is not any of above then it is an identifier
        return new Token(TokenType.TOK_Identifier, lexeme);
    }

    // used to get index for transition table
    private static int stateToInt(State state){
        switch (state){
            case S00: return 0;
            case S01: return 1;
            case S02: return 2;
            case S03: return 3;
            case S04: return 4;
            case S05: return 5;
            case S06: return 6;
            case S07: return 7;
            case S08: return 8;
            case S09: return 9;
            case S10: return 10;
            case S11: return 11;
            case S12: return 12;
            case S13: return 13;
            case S14: return 14;
            case S15: return 15;
            case S16: return 16;
            case S17: return 17;
            case S18: return 18;
            case SERR: return 19;
        }
        return -1;
    }

    private static int findClassifier(char nextChar) {
        if(Character.isLetter(nextChar)){
            return Classifier.LETTER;
        }

        if(Character.isDigit(nextChar)){
            return Classifier.DIGIT;
        }

        switch (nextChar){
            case '\u001a':
                return Classifier.EOF;
            case '<':
            case '>':
                return Classifier.COMPARISON;
            case '=':
                return Classifier.EQUALS;
            case '!':
                return Classifier.EXCLAMATION;
            case '+':
            case '-':
                return Classifier.ADD_AND_SUB;
            case '*':
                return Classifier.ASTERISK;
            case '/':
                return Classifier.SLASH;
            case '\n':
                return Classifier.NEW_LINE;
            case '.':
                return Classifier.DOT;
            case '_':
                return Classifier.UNDERSCORE;
            case '}':
            case '{':
            case '(':
            case ')':
            case ':':
            case ';':
            case ',':
            case '\'':
                return Classifier.PUNCTUATION;
            default:
                return Classifier.OTHER;
        }
    }

    // checks if state is final state or not
    private boolean isFinalState(State state){
        for(State finalState : finalStates){
            if(state == finalState){
                return true;
            }
        }
        return false;
    }

    public String tokenToString(TokenType tokenType){
        switch(tokenType){
            case TOK_EOF: return "TOK_EOF";
            case TOK_LeftBracket: return "TOK_LeftBracket";
            case TOK_RightBracket: return "TOK_RightBracket";
            case TOK_LeftCurly: return "TOK_LeftCurly";
            case TOK_RightCurly: return "TOK_RightCurly";
            case TOK_Multiplication: return "TOK_Multiplication";
            case TOK_Addition: return "TOK_Addition";
            case TOK_Relation: return "TOK_Relation";
            case TOK_Comma: return "TOK_Comma";
            case TOK_Colon: return "TOK_Colon";
            case TOK_Semicolon: return "TOK_Semicolon";
            case TOK_Equal: return "TOK_Equal";
            case TOK_BooleanValue: return "TOK_BooleanValue";
            case TOK_FloatValue: return "TOK_FloatValue";
            case TOK_IntegerValue: return "TOK_IntegerValue";
            case TOK_BooleanKeyword: return "TOK_BooleanKeyword";
            case TOK_FloatKeyword: return "TOK_FloatKeyword";
            case TOK_IntegerKeyWord: return "TOK_IntegerKeyWord";
            case TOK_Identifier: return "TOK_Identifier";
            case TOK_Let: return "TOK_Let";
            case TOK_Print: return "TOK_Print";
            case TOK_Return: return "TOK_Return";
            case TOK_Function: return "TOK_Function";
            case TOK_If: return "TOK_If";
            case TOK_Else: return "TOK_Else";
            case TOK_For: return "TOK_For";
            case TOK_Logic: return "TOK_Logic";
            case TOK_Comment: return "TOK_Comment";
            default: return "Error";
        }
    }

    // getting contents of file and putting them in a string
    private static String fileToString(String filePath){
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)){
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            System.out.println("Error when loading contents of file.");
            e.printStackTrace();
            System.exit(1);
        }

        return contentBuilder.toString();
    }
}
