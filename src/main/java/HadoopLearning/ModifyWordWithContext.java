package HadoopLearning;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;
import lombok.Data;

@Data
public class ModifyWordWithContext extends BaseOperation<Tuple> implements Function<Tuple> {

    public ModifyWordWithContext(Fields outputField){
        super(1, outputField);
    }
    @Override
    public void prepare(FlowProcess flowProcess, OperationCall<Tuple> call ){
        System.out.println("prepared");
        call.setContext( Tuple.size( 1 ) );
    }

    @Override
    public void operate(FlowProcess flowProcess, FunctionCall<Tuple> call){
        TupleEntry arguments = call.getArguments();

        // get our previously created Tuple
        Tuple result = call.getContext();

        result.set(0,arguments.getString(0)+"_common");

        // return the result Tuple
        call.getOutputCollector().add( result );
        System.out.println("operated");
    }

    @Override
    public void cleanup(FlowProcess flowProcess, OperationCall<Tuple> call ){
        call.setContext(null);
        System.out.println("completed");
    }
}
