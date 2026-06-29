package TP;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * ANALYSE 1 : Profil horaire.
 * Cle = heure de la journee (00..23), extraite de la colonne Time (hh:mm:ss).
 * Valeur = stats sur Global_active_power.
 * Reutilise PowerConsumptionCombiner et PowerConsumptionReducer.
 */
public class HourlyMapper extends Mapper<Object, Text, Text, PowerStatsWritable> {

    private final Text hourKey = new Text();
    private final PowerStatsWritable stats = new PowerStatsWritable();

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        if (line.startsWith("Date")) {
            return;
        }

        String[] f = line.split(";");
        if (f.length < 3) {
            return;
        }

        String time = f[1];           // hh:mm:ss
        String activePower = f[2];

        if (activePower.equals("?") || activePower.isEmpty()) {
            return;
        }

        try {
            double power = Double.parseDouble(activePower);
            String[] t = time.split(":");
            if (t.length < 1) {
                return;
            }
            String hour = t[0];       // "00" .. "23"

            hourKey.set(hour);
            stats.set(power);
            context.write(hourKey, stats);
        } catch (NumberFormatException e) {
            // ignore
        }
    }
}
