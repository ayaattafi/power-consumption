package TP;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Locale;

/**
 * Agrege par mois et ecrit : total, nb manquants, taux (%).
 * Sert aussi de Combiner ? Non : sortie finale = Text, donc on n'utilise
 * pas de combiner ici (le volume reste raisonnable).
 */
public class QualityReducer extends Reducer<Text, QualityWritable, Text, Text> {

    private final Text result = new Text();

    @Override
    public void reduce(Text key, Iterable<QualityWritable> values, Context context)
            throws IOException, InterruptedException {

        QualityWritable agg = new QualityWritable();
        for (QualityWritable v : values) {
            agg.merge(v);
        }

        String line = String.format(
                Locale.US,
                "total=%d\tmanquants=%d\ttaux=%.2f%%",
                agg.getTotal(), agg.getMissing(), agg.getRate());

        result.set(line);
        context.write(key, result);
    }
}
