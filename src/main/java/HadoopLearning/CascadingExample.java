package HadoopLearning;


import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.operation.Aggregator;
import cascading.operation.Function;
import cascading.operation.aggregator.Count;
import cascading.operation.regex.RegexGenerator;
import cascading.pipe.Each;
import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.property.AppProps;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

import java.util.Properties;


/**
 * Example is about  Copy data from input_path to output_path
 * WORD COUNT
 */
public class CascadingExample {
    public static void main(String[] args) {
        System.out.println("inside");
        System.out.println(args[1]);
        String docPath = args[0];
        String wcPath = args[1];

        Properties props = new Properties();
        AppProps.setApplicationJarClass(props, CascadingExample.class);
        HadoopFlowConnector flowConnector = new HadoopFlowConnector(props);

        Scheme sourceScheme = new TextLine(new Fields("line"));
        Tap source = new Hfs(sourceScheme, docPath);

        Scheme sinkScheme = new TextLine(new Fields("word", "count"));
        Tap sink = new Hfs(sinkScheme, wcPath, SinkMode.REPLACE);

        Pipe assemble = new Pipe("wordCount");
        String regex = "(?<!\\pL)(?=\\pL)[^ ]*(?<=\\pL)(?!\\pL)";
        Function function = new RegexGenerator(new Fields("word"), regex);


        assemble = new Each(assemble, new Fields("line"), function);


        /**
         * lets say we want to drop "bet" from words  & give output
         */
        Function appendWords = new AppendWordsWithoutContext(new Fields("words"));
        assemble = new Each(assemble, appendWords);

        Function modifyWordWithContext = new ModifyWordWithContext(new Fields("words"));
        assemble = new Each(assemble, modifyWordWithContext);


        assemble = new GroupBy(assemble, new Fields("words"));

        Aggregator count = new Count(new Fields("count"));
        assemble = new Every(assemble, count);
        flowConnector.connect("word-count", source, sink, assemble).complete();
        System.out.println("Done");
    }
}
