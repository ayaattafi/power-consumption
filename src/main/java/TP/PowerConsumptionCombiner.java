package TP;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PowerConsumptionCombiner
        extends Reducer<Text, PowerStatsWritable, Text, PowerStatsWritable> {

    @Override
    public void reduce(Text key, Iterable<PowerStatsWritable> values, Context context)
            throws IOException, InterruptedException {

        PowerStatsWritable result = new PowerStatsWritable();
        for (PowerStatsWritable v : values) {
            result.merge(v);
        }
        context.write(key, result);
    }
}