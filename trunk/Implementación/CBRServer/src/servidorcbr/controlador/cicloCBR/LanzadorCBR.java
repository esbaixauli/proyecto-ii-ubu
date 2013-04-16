package servidorcbr.controlador.cicloCBR;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.ExecutionException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HRegionPartitioner;
import org.apache.hadoop.hbase.mapreduce.IdentityTableMapper;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.hbase.HbaseFacade;

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

	public Collection<CBRCase> retrieve (TipoCaso tc, HashMap<String,Serializable> query) throws IOException {
		System.out.println("Configurando job:");
		// create a configuration
		Configuration conf = new Configuration();
	    conf.addResource(new Path("/etc/hadoop/core-site.xml"));
	    conf.addResource(new Path("/etc/hadoop/hdfs-site.xml"));
		conf.set("tipocaso", tc.getNombre());
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
		
		// here you have to put your reducer class
		job.setReducerClass(ReducerRetrieval.class);
		// here you have to set the jar which is containing your 
		// map/reduce class, so you can use the mapper class
		job.setJarByClass(ReducerRetrieval.class);

		// key/value of your reducer output
		job.setOutputKeyClass(ImmutableBytesWritable.class);
		job.setOutputValueClass(Text.class);
		// same with output
		job.setOutputFormatClass(NullOutputFormat.class);
		
		// partitioner HRegionPartitioner para usar IdentityTableMapper y que lo divida por regiones
		job.setPartitionerClass(HRegionPartitioner.class);
		int nregions=0;
		try {
			nregions = HbaseFacade.getInstance().getNRegions(Bytes.toBytes(tc.getNombre()));
		} catch (PersistenciaException e2) {
			e2.printStackTrace();
		}
		try {
			if (job.getNumReduceTasks() > nregions) {
				if (nregions > 0) {
					job.setNumReduceTasks(nregions);
				} else {
					job.setNumReduceTasks(1);
				}
			}
		} catch (IllegalStateException e3) {
			e3.printStackTrace();
		}
		
		try {
			System.out.println("- Mapper: "+job.getMapperClass().getSimpleName());
			System.out.println("- Partitioner: "+job.getPartitionerClass().getSimpleName());
			System.out.println("- Reducer: "+job.getReducerClass().getSimpleName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Preparado para lanzar job para " +tc.getNombre()+", nº regiones: "+nregions);

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
			// "tcs/" se traduce en hdfs a "/user/$USER/tcs/"
			Path outFile = new Path("tcs/");
			if (!fs.exists(outFile)) {
				fs.mkdirs(outFile);
			}
			outFile = new Path(outFile, tc.getNombre()+".tc");
			FSDataOutputStream outfs = fs.create(outFile, true);
			ObjectOutputStream oos = new ObjectOutputStream(outfs);
			oos.writeObject(tc);
			oos.flush();
			oos.close();
			outfs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
