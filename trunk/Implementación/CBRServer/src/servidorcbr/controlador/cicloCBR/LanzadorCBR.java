package servidorcbr.controlador.cicloCBR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.IdentityTableMapper;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import servidorcbr.modelo.TipoCaso;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseBaseFilter;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.ExecutionException;
import jcolibri.exception.InitializingException;

public class LanzadorCBR implements StandardCBRApplication {

	@Override
	public void configure() throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cycle(CBRQuery arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postCycle() throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<CBRCase> retrieve (TipoCaso tc, HashMap<String,Serializable> query) {
		// create a configuration
		Configuration conf = new Configuration();
		// escribe el tipo de caso en HDFS para leerlo en el reducer
		escribeTipoCasoHDFS(tc, conf);
		// create a new job based on the configuration
		Job job = null;
		try {
			job = new Job(conf);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// here you have to put your mapper class
		job.setMapperClass(IdentityTableMapper.class);
		// here you have to put your reducer class
		job.setReducerClass(ReducerRetrieval.class);
		// here you have to set the jar which is containing your 
		// map/reduce class, so you can use the mapper class
		job.setJarByClass(IdentityTableMapper.class);
		
		Scan scan = new Scan();
		scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
		scan.setCacheBlocks(false);  // don't set to true for MR jobs
		// set other scan attrs

		try {
			TableMapReduceUtil.initTableMapperJob(
			  tc.getNombre(),        // input HBase table name
			  scan,             // Scan instance to control CF and attribute selection
			  IdentityTableMapper.class,   // mapper
			  ImmutableBytesWritable.class,             // mapper output key
			  Result.class,             // mapper output value
			  job);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// key/value of your reducer output
		job.setOutputKeyClass(ImmutableBytesWritable.class);
		job.setOutputValueClass(Text.class);
		// same with output
		job.setOutputFormatClass(NullOutputFormat.class);

		// this waits until the job completes and prints debug out to STDOUT or whatever
		// has been configured in your log4j properties.
		try {
			job.waitForCompletion(true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void escribeTipoCasoHDFS (TipoCaso tc, Configuration conf) {
		try {
			FileSystem fs = FileSystem.get(conf);
			Path outFile = new Path("/"+tc.getNombre()+".tc");
			FSDataOutputStream outfs = fs.create(outFile);
			ObjectOutputStream oos = new ObjectOutputStream(outfs);
			oos.writeObject(tc);
			outfs.close();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
