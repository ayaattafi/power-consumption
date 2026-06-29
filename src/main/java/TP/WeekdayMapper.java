package TP;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * ANALYSE 2 : Semaine vs week-end.
 * Cle = "SEMAINE" ou "WEEKEND" (calcule a partir de la date dd/mm/yyyy).
 * Valeur = stats sur Global_active_power.
 * Reutilise PowerConsumptionCombiner et PowerConsumptionReducer.
 */
public class WeekdayMapper extends Mapper<Object, Text, Text, PowerStatsWritable> {

    private final Text dayTypeKey = new Text();
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

        String date = f[0];           // dd/mm/yyyy
        String activePower = f[2];

        if (activePower.equals("?") || activePower.isEmpty()) {
            return;
        }

        try {
            double power = Double.parseDouble(activePower);

            String[] d = date.split("/");
            if (d.length != 3) {
                return;
            }
            int day = Integer.parseInt(d[0]);
            int month = Integer.parseInt(d[1]);
            int year = Integer.parseInt(d[2]);

            DayOfWeek dow = LocalDate.of(year, month, day).getDayOfWeek();
            String type = (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY)
                    ? "WEEKEND" : "SEMAINE";

            dayTypeKey.set(type);
            stats.set(power);
            context.write(dayTypeKey, stats);
        } catch (Exception e) {
            // ignore
        }
    }
}
