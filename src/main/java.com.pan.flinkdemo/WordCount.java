import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author 潘聪
 * @description
 * @date 2021/3/9 21:09
 */
public class WordCount {
    public static void main(String[] args) throws Exception {

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> textLine = env.readTextFile("src/main/resources/word.txt");
        AggregateOperator<Tuple2<String, Integer>> sum = textLine.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for (String word : s.split("||")) {
                    Tuple2<String, Integer> wordTuple = new Tuple2<>(word, 1);
                    collector.collect(wordTuple);
                }
            }
        }).groupBy(0).sum(1);
        sum.print();
    }
}
