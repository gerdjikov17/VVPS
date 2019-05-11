import java.util.ArrayList;

public class AlgorithmLine {

    ArrayList<Integer> Values;

    AlgorithmLine(){
        Values = new ArrayList<>();
    }

    public void Add(Integer toAdd){
        Values.add(toAdd);
    }

    public Boolean IsEmpty(){
        return Values.isEmpty();
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        for(Integer value : Values){
            stringBuilder.append(value).append(" ");
        }

        return (stringBuilder.toString()).trim();
    }
}
