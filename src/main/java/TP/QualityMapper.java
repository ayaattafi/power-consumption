package TP;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Cle = mois (aaaa-mm). Valeur = (1 ligne, manquante ? 1 : 0).
 * Une ligne est consideree "manquante" si elle contient un '?'.
 */
public class QualityMapper extends Mapper<Object, Text, Text, QualityWritable> {

    private final Text monthKey = new Text();
    private final QualityWritable q = new QualityWritable();

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

        String[] d = f[0].split("/");   // dd/mm/yyyy
        if (d.length != 3) {
            return;
        }
        String yearMonth = d[2] + "-" + d[1];

        boolean missing = line.contains("?");

        monthKey.set(yearMonth);
        q.set(1L, missing ? 1L : 0L);
        context.write(monthKey, q);
    }
}
