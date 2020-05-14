package HadoopLearning;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppendWordsWithoutContext extends BaseOperation implements Function {
    public final Pattern regex = Pattern.compile("bet.*");
    public AppendWordsWithoutContext(){

        super(1,new Fields("words"));
    }
    public Set<String> filterWords = new HashSet<>();
    public AppendWordsWithoutContext(Fields fieldDec){
        // fieldDec is the output fields
        super(1, fieldDec);
    }
    @Override
    public void operate(FlowProcess flowProcess, FunctionCall functionCall){
        TupleEntry arguments = functionCall.getArguments();
        Tuple result = new Tuple();
        String newWord = arguments.getString(0);
        Matcher matcher = regex.matcher(newWord);
        if(matcher.matches()){
             newWord +=  "_modified";
        }

        result.add(newWord);
        functionCall.getOutputCollector().add( result );
    }

}
