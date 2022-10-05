package semantic;

import java.util.ArrayList;

public class Symbol {
    public String identifier;
    public String declareType;
    public String literalType;
    public Symbol next;

    public ArrayList<String> functionParamTypes;

    public Symbol(){
        next = null;
    }

    public Symbol(String identifier, String declareType, String literalType){
        this.identifier = identifier;
        this.declareType = declareType;
        this.literalType = literalType;
    }
}
