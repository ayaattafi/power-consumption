package TP;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Lancement : hadoop jar powerconsumption-1.jar TP.HourlyDriver power_input out_hourly
 */
public class HourlyDriver {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: HourlyDriver <input> <output>");
            System.exit(2);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "hourly power profile");
        job.setJarByClass(HourlyDriver.class);

        job.setMapperClass(HourlyMapper.class);
        job.setCombinerClass(PowerConsumptionCombiner.class);
        job.setReducerClass(PowerConsumptionReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(PowerStatsWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
