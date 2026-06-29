package TP;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * ANALYSE 3 : Repartition par sous-compteur (quel poste consomme le plus).
 * Pour chaque ligne, on emet 3 paires :
 *   1-Cuisine            -> Sub_metering_1 (Wh)
 *   2-Buanderie          -> Sub_metering_2 (Wh)
 *   3-ChauffeEau-Clim    -> Sub_metering_3 (Wh)
 * Le Reducer somme tout sur la periode.
 */
public class SubMeteringMapper extends Mapper<Object, Text, Text, DoubleWritable> {

    private final Text catKey = new Text();
    private final DoubleWritable val = new DoubleWritable();

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        if (line.startsWith("Date")) {
            return;
        }

        String[] f = line.split(";");
        if (f.length < 9) {
            return;
        }

        String s1 = f[6];
        String s2 = f[7];
        String s3 = f[8];

        if (s1.equals("?") || s2.equals("?") || s3.equals("?")) {
            return;
        }

        try {
            double v1 = Double.parseDouble(s1);
            double v2 = Double.parseDouble(s2);
            double v3 = Double.parseDouble(s3);

            catKey.set("1-Cuisine");          val.set(v1); context.write(catKey, val);
            catKey.set("2-Buanderie");        val.set(v2); context.write(catKey, val);
            catKey.set("3-ChauffeEau-Clim");  val.set(v3); context.write(catKey, val);
        } catch (NumberFormatException e) {
            // ignore
        }
    }
}
