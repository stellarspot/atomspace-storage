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

    // Platform.startup(...) method is not available in JDK 8.
    private static PlotterProperties PROPS;

    @Override
    public void start(Stage stage) {

        stage.setTitle(PROPS.title);

        List<LineChart<Number, Number>> lineCharts =
                getLineCharts(PROPS);
        HBox hbox = new HBox(lineCharts.toArray(new LineChart[]{}));

        Scene scene = new Scene(hbox, PROPS.width, PROPS.height);
        stage.setScene(scene);
        stage.show();
    }

    List<LineChart<Number, Number>> getLineCharts(PlotterProperties props) {

        List<LineChart<Number, Number>> charts = new ArrayList<>();

        Bounds bounds = getBounds(props.measurements);

        if (props.sameChart) {
            charts.add(getLineChart(bounds, props));
        }

        for (Map.Entry<String, List<ParamWithTime>> entry : props.measurements.entrySet()) {
            String name = entry.getKey();
            List<ParamWithTime> values = entry.getValue();

            LineChart<Number, Number> lineChart = props.sameChart
                    ? charts.get(0)
                    : getLineChart(getBounds(values), props);

            lineChart.getData().addAll(getSeries(name, values));
            if (!props.sameChart) {
                charts.add(lineChart);
            }
        }

        return charts;
    }

    LineChart<Number, Number> getLineChart(Bounds b, PlotterProperties props) {

        final NumberAxis xAxis = new NumberAxis(b.xMin, b.xMax, (b.xMax - b.xMin) / 8);
        final NumberAxis yAxis = new NumberAxis(b.yMin, b.yMax, (b.yMax - b.yMin) / 8);
        yAxis.setLabel(String.format("Time(%s)", props.timeUnits));
        xAxis.setLabel(props.label);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(props.title);
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

    public static void showMeasurements(PlotterProperties props) {
        // Platform.startup(...) method is not available in JDK 8.
        PROPS = props;
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

//        PlotterProperties props = PlotterProperties.sameChart(measurements);
        PlotterProperties props = PlotterProperties.differentCharts(measurements);

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
        public int width = 700;
        public int height = 300;
        public String title = "";
        public String label = "";
        public String timeUnits = "ms";
        public boolean sameChart = true;
        public Map<String, List<PerformanceResult.ParamWithTime>> measurements;

        public static PlotterProperties sameChart(Map<String, List<ParamWithTime>> measurements) {
            PlotterProperties props = new PlotterProperties();
            props.sameChart = true;
            props.measurements = measurements;
            return props;
        }

        public static PlotterProperties differentCharts(Map<String, List<ParamWithTime>> measurements) {
            PlotterProperties props = new PlotterProperties();
            props.sameChart = false;
            props.measurements = measurements;
            return props;
        }
    }
}
