package TP;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Locale;

public class PowerConsumptionReducer
        extends Reducer<Text, PowerStatsWritable, Text, Text> {

    private final Text result = new Text();

    @Override
    public void reduce(Text key, Iterable<PowerStatsWritable> values, Context context)
            throws IOException, InterruptedException {

        PowerStatsWritable agg = new PowerStatsWritable();
        for (PowerStatsWritable v : values) {
            agg.merge(v);
        }

        String line = String.format(
                Locale.US,
                "count=%d\tavg=%.3f\tmin=%.3f\tmax=%.3f\tsum=%.3f",
                agg.getCount(),
                agg.getAverage(),
                agg.getMin(),
                agg.getMax(),
                agg.getSum());

        result.set(line);
        context.write(key, result);
    }
}