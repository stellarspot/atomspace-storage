package atomspace.storage.performance.result;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static atomspace.storage.performance.result.PerformanceResult.ParamWithTime;

public class PerformanceResultPlotter extends Application {


    private static int WIDTH = 700;
    private static int HEIGHT = 300;
    private static String TITLE = "TITLE";
    private static String LABEL = "LABEL";
    private static String TIME_UNITS = "ms";
    private static boolean SAME_CHART = true;
    private static Map<String, List<ParamWithTime>> MEASUREMENTS = new HashMap<>();


    @Override
    public void start(Stage stage) {

        stage.setTitle(TITLE);

        List<LineChart<Number, Number>> lineCharts = getLineCharts(SAME_CHART, MEASUREMENTS);
        HBox hbox = new HBox(lineCharts.toArray(new LineChart[]{}));

        Scene scene = new Scene(hbox, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    List<LineChart<Number, Number>> getLineCharts(boolean sameChart, Map<String, List<ParamWithTime>> measurements) {

        List<LineChart<Number, Number>> charts = new ArrayList<>();

        Bounds bounds = getBounds(measurements);

        if (sameChart) {
            charts.add(getLineChart(bounds));
        }

        for (Map.Entry<String, List<ParamWithTime>> entry : MEASUREMENTS.entrySet()) {
            String name = entry.getKey();
            List<ParamWithTime> values = entry.getValue();

            LineChart<Number, Number> lineChart = sameChart
                    ? charts.get(0)
                    : getLineChart(getBounds(values));

            lineChart.getData().addAll(getSeries(name, values));
            if (!sameChart) {
                charts.add(lineChart);
            }
        }

        return charts;
    }

    LineChart<Number, Number> getLineChart(Bounds b) {

        final NumberAxis xAxis = new NumberAxis(b.xMin, b.xMax, (b.xMax - b.xMin) / 8);
        final NumberAxis yAxis = new NumberAxis(b.yMin, b.yMax, (b.yMax - b.yMin) / 8);
        yAxis.setLabel(String.format("Time(%s)", TIME_UNITS));
        xAxis.setLabel(LABEL);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(TITLE);
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
                                        Map<String, List<PerformanceResult.ParamWithTime>> measurements) {
        // Platform.startup(...) method is not available in JDK 8.
        MEASUREMENTS.putAll(measurements);
        TITLE = title;
        LABEL = label;
        TIME_UNITS = timeUnits;

        launch();
    }

    public static void showMeasurements(PlotterProperties props) {
        // Platform.startup(...) method is not available in JDK 8.
        MEASUREMENTS.putAll(props.measurements);
        TITLE = props.title;
        LABEL = props.label;
        TIME_UNITS = props.timeUnits;
        SAME_CHART = props.sameChart;

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

//        PlotterProperties props = PlotterProperties.sameChartMs("Chart", "sample", measurements);
        PlotterProperties props = PlotterProperties.differentChartsMs("Chart", "sample", measurements);

        showMeasurements(props);
    }

    Bounds getBounds(Map<String, List<ParamWithTime>> measurements) {
        Bounds bounds = new Bounds(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);

        for (List<ParamWithTime> values : measurements.values()) {
            bounds = bounds.union(getBounds(values));
        }

        return bounds;
    }


    Bounds getBounds(List<ParamWithTime> values) {
        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;

        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        for (ParamWithTime value : values) {

            int x = Integer.parseInt(value.param);
            double y = value.time;

            if (x < xMin) xMin = x;
            if (x > xMax) xMax = x;
            if (y < yMin) yMin = y;
            if (y > yMax) yMax = y;
        }

        return new Bounds(xMin, xMax, yMin, yMax);
    }

    static class Bounds {
        final double xMin;
        final double xMax;

        final double yMin;
        final double yMax;

        public Bounds(double xMin, double xMax, double yMin, double yMax) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
        }

        Bounds union(Bounds that) {

            return new Bounds(
                    Math.min(this.xMin, that.xMin),
                    Math.max(this.xMax, that.xMax),
                    Math.min(this.yMin, that.yMin),
                    Math.max(this.yMax, that.yMax));
        }
    }

    public static class PlotterProperties {
        public final String title;
        public final String label;
        public final String timeUnits;
        public final boolean sameChart;
        public final Map<String, List<PerformanceResult.ParamWithTime>> measurements;

        public PlotterProperties(String title, String label, String timeUnits, boolean sameChart, Map<String, List<ParamWithTime>> measurements) {
            this.title = title;
            this.label = label;
            this.timeUnits = timeUnits;
            this.sameChart = sameChart;
            this.measurements = measurements;
        }

        public static PlotterProperties sameChartMs(String title, String label, Map<String, List<ParamWithTime>> measurements) {
            return new PlotterProperties(title, label, "ms", true, measurements);
        }

        public static PlotterProperties differentChartsMs(String title, String label, Map<String, List<ParamWithTime>> measurements) {
            return new PlotterProperties(title, label, "ms", false, measurements);
        }
    }
}
