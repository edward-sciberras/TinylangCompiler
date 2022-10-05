package semantic;

public class SymbolTable {
    public static final int MAX_SIZE = 100;

    private int index;
    Symbol[] head = new Symbol[MAX_SIZE];

    public SymbolTable(){
        for(int i = 0; i < MAX_SIZE; i++){
            head[i] = null;
        }
    }

    public int hashFunction(String identifier){
        int total = 0;
        for(int i = 0; i < identifier.length(); i++){
            total += identifier.charAt(i);
        }
        return (total % MAX_SIZE);
    }

    public void insert(String identifier, String declareType, String literalType){
        index = hashFunction(identifier);
        Symbol symbol = new Symbol(identifier, declareType, literalType);

        if(head[index] == null){
            head[index] = symbol;
        } else {
            Symbol start = head[index];
            while(start.next != null){
                start = start.next;
            }
            start.next = symbol;
        }
    }

    public Symbol search(String identifier){
        index = hashFunction(identifier);
        Symbol start = head[index];

        if(start == null){
            return null;
        }

        while(start != null){
            if(start.identifier.equals(identifier)){
                return start;
            }
            start = start.next;
        }
        return null;
    }
}
