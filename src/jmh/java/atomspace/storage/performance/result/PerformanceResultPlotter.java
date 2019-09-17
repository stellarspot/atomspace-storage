package atomspace.storage.performance.result;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static atomspace.storage.performance.result.PerformanceResult.ParamWithTime;

public class PerformanceResultPlotter extends Application {


    private static String TITLE = "TITLE";
    private static String LABEL = "LABEL";
    private static String TIME_UNITS = "ms";
    private static Map<String, List<ParamWithTime>> MEASUREMENTS = new HashMap<>();


    @Override
    public void start(Stage stage) {

        stage.setTitle(TITLE);

        final LineChart<Number, Number> lineChart = getLineChartWithBounds(MEASUREMENTS);


        for (Map.Entry<String, List<ParamWithTime>> entry : MEASUREMENTS.entrySet()) {
            String name = entry.getKey();
            List<ParamWithTime> values = entry.getValue();
            lineChart.getData().addAll(getSeries(name, values));
        }

        Scene scene = new Scene(lineChart, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    LineChart<Number, Number> getLineChartWithBounds(Map<String, List<ParamWithTime>> measurements) {

        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;

        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        for (List<ParamWithTime> values : measurements.values()) {
            for (ParamWithTime value : values) {

                int x = Integer.parseInt(value.param);
                double y = value.time;

                if (x < xMin) xMin = x;
                if (x > xMax) xMax = x;
                if (y < yMin) yMin = y;
                if (y > yMax) yMax = y;
            }
        }


        final NumberAxis xAxis = new NumberAxis(xMin, xMax, (xMax - xMin) / 8);
        final NumberAxis yAxis = new NumberAxis(yMin, yMax, (yMax - yMin) / 8);
        yAxis.setLabel("Time(ms)");
        xAxis.setLabel(LABEL);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Chart");

        return lineChart;
    }


    static XYChart.Series getSeries(String name, List<ParamWithTime> values) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        for (ParamWithTime value : values) {
            int x = Integer.parseInt(value.param);
            double y = value.time;
            series.getData().add(new XYChart.Data(x, y));
        }

        return series;
    }

    public static void showMeasurements(String title,
                                        String label,
                                        String timeUnits,
                                        Map<String, List<PerformanceResult.ParamWithTime>> results) {
        // Platform.startup(...) method is not available in JDK 8.
        MEASUREMENTS.putAll(results);
        TITLE = title;
        LABEL = label;
        TIME_UNITS = timeUnits;

        launch();
    }

    public static void main(String[] args) {

        Map<String, List<ParamWithTime>> measurements = new HashMap<>();

        List<ParamWithTime> list = new ArrayList<>();
        list.add(new ParamWithTime("2", 4));
        list.add(new ParamWithTime("4", 8));
        list.add(new ParamWithTime("6", 12));
        list.add(new ParamWithTime("8", 16));
        measurements.put("testA", list);

        list = new ArrayList<>();
        list.add(new ParamWithTime("2", 4));
        list.add(new ParamWithTime("4", 8));
        list.add(new ParamWithTime("6", 16));
        list.add(new ParamWithTime("8", 32));
        measurements.put("testB", list);

        showMeasurements("Chart", "sample", "ms", measurements);
    }
}
