package atomspace.storage.performance.result;

import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;

public class PerformanceResult {

    public static Map<String, List<ParamWithTime>> runJMHTest(Class cls, String paramName) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(cls.getSimpleName())
                .build();

        Collection<RunResult> results = new Runner(opt).run();

        final Map<String, List<ParamWithTime>> measurements = new HashMap<>();

        for (RunResult result : results) {

            Result r = result.getPrimaryResult();
            String label = r.getLabel();
            String param = result.getParams().getParam(paramName);
            double score = r.getScore();

            List<ParamWithTime> list = measurements.computeIfAbsent(label, key -> new ArrayList<>());

            list.add(new ParamWithTime(param, score));
        }

        return measurements;
    }

    public static class ParamWithTime {
        final String param;
        final double time;

        public ParamWithTime(String param, double time) {
            this.param = param;
            this.time = time;
        }

        @Override
        public String toString() {
            return String.format("param: %s, time: %f", param, time);
        }
    }
}
