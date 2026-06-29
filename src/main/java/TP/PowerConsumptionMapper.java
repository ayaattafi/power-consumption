package TP;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PowerConsumptionMapper
        extends Mapper<Object, Text, Text, PowerStatsWritable> {

    private final Text monthKey = new Text();
    private final PowerStatsWritable stats = new PowerStatsWritable();

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        if (line.startsWith("Date")) {
            return;
        }

        String[] fields = line.split(";");
        if (fields.length < 3) {
            return;
        }

        String date = fields[0];
        String activePower = fields[2];

        if (activePower.equals("?") || activePower.isEmpty()) {
            return;
        }

        try {
            double power = Double.parseDouble(activePower);

            String[] parts = date.split("/");
            if (parts.length != 3) {
                return;
            }
            String yearMonth = parts[2] + "-" + parts[1];

            monthKey.set(yearMonth);
            stats.set(power);
            context.write(monthKey, stats);

        } catch (NumberFormatException e) {
            // ligne mal formee : ignoree
        }
    }
}