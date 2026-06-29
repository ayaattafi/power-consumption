package TP;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PowerStatsWritable implements Writable {

    private long count;
    private double sum;
    private double min;
    private double max;

    public PowerStatsWritable() {
        this.count = 0L;
        this.sum = 0.0;
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
    }

    public void set(double value) {
        this.count = 1L;
        this.sum = value;
        this.min = value;
        this.max = value;
    }

    public void merge(PowerStatsWritable other) {
        this.count += other.count;
        this.sum += other.sum;
        if (other.min < this.min) {
            this.min = other.min;
        }
        if (other.max > this.max) {
            this.max = other.max;
        }
    }

    public long getCount() {
        return count;
    }

    public double getSum() {
        return sum;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getAverage() {
        return count == 0 ? 0.0 : sum / count;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(count);
        out.writeDouble(sum);
        out.writeDouble(min);
        out.writeDouble(max);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        count = in.readLong();
        sum = in.readDouble();
        min = in.readDouble();
        max = in.readDouble();
    }
}