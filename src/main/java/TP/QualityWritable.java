package TP;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * ANALYSE 4 : Qualite des donnees.
 * Transporte le nombre total de lignes et le nombre de lignes contenant
 * au moins une valeur manquante ('?'), par mois.
 */
public class QualityWritable implements Writable {

    private long total;
    private long missing;

    public QualityWritable() {
        this.total = 0L;
        this.missing = 0L;
    }

    public void set(long total, long missing) {
        this.total = total;
        this.missing = missing;
    }

    public void merge(QualityWritable other) {
        this.total += other.total;
        this.missing += other.missing;
    }

    public long getTotal() {
        return total;
    }

    public long getMissing() {
        return missing;
    }

    public double getRate() {
        return total == 0 ? 0.0 : (100.0 * missing / total);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(total);
        out.writeLong(missing);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        total = in.readLong();
        missing = in.readLong();
    }
}
