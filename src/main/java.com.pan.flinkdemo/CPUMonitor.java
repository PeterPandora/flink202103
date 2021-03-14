import entity.CPU;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author 潘聪
 * @description
 * @date 2021/3/14 21:01
 */
public class CPUMonitor {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        CpuLogSource cpuLogSource = new CpuLogSource();
        DataStreamSource<CPU> netLogDataStreamSource = env.addSource(cpuLogSource);
        SingleOutputStreamOperator<CPU> cpuId = netLogDataStreamSource.keyBy("cpuId")
                .reduce((curr, next) -> (next.getTemperature() - curr.getTemperature()) > 0 ? next : curr);
        cpuId.print();
        env.execute();
    }

    public static class CpuLogSource implements SourceFunction<CPU> {

        private boolean runing = true;

        @Override
        public void run(SourceContext<CPU> ctx) throws Exception {
            Map<String, Double> cpuMap = new HashMap<>();
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                cpuMap.put("cpu" + i, 60 + random.nextGaussian() * 20);
            }
            while (runing) {
                for (String cpuId : cpuMap.keySet()) {
                    Double newtemp = cpuMap.get(cpuId) + random.nextGaussian();
                    CPU cpu = new CPU(cpuId, newtemp, new Date());
                    ctx.collect(cpu);
                }
                Thread.sleep(1000);
            }
        }

        @Override
        public void cancel() {
            runing = false;
        }
    }
}
