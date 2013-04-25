package servidorcbr.controlador.cicloCBR;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;

import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorTecnicaRetrieval;
import servidorcbr.controlador.generadorClases.RellenadorClases;
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

	public Collection<CBRCase> retrieve (TipoCaso tc, HashMap<String,Serializable> query) {
		// create a configuration
		Configuration conf = new Configuration();
	    conf.addResource(new Path("/etc/hadoop/core-site.xml"));
	    conf.addResource(new Path("/etc/hadoop/hdfs-site.xml"));
		conf.set("tipocaso", tc.getNombre());
		// escribe el tipo de caso y el query en HDFS para leerlo en el reducer
		escribeTipoCasoHDFS(tc, conf);
		String queryFile = escribeQueryHDFS(query, tc, conf);
		conf.set("query", queryFile);
		
		// crea la tabla temporal en HBase en la que escribirá el reducer
		int i = 0;
		String outputTable = null;
		try {
			HbaseFacade hbf = HbaseFacade.getInstance();
			do {
				outputTable = tc.getNombre() + i;
				i++;
			} while (!hbf.createTable(outputTable));
		} catch (PersistenciaException e2) {
			e2.printStackTrace();
		}
		System.out.println("Tabla de salida del reducer: "+outputTable);
		
		// create a new job based on the configuration
		Job job = null;
		try {
			job = new Job(conf);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Scan scan = new Scan();
		scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
		scan.setCacheBlocks(false);  // don't set to true for MR jobs

		try {
			TableMapReduceUtil.initTableMapperJob(
					tc.getNombre(),        			// input HBase table name
					scan,             // Scan instance to control CF and attribute selection
					MapperRetrieval.class,
					ImmutableBytesWritable.class,   // mapper output key
					Result.class,             		// mapper output value
					job);
			TableMapReduceUtil.initTableReducerJob(
					outputTable, 
					ReducerRetrieval.class, 
					job);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// here you have to set the jar which is containing your 
		// map/reduce class, so you can use the mapper class
		job.setJarByClass(ReducerRetrieval.class);
		
		// this waits until the job completes and prints debug out to STDOUT or whatever
		// has been configured in your log4j properties.
		try {
			job.waitForCompletion(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// El fichero del query serializado ya no es necesario
		borraQueryHDFS(queryFile, conf);
		
		List<HashMap<String,Serializable>> resultadoHash = null;
		try {
			HbaseFacade hbf = HbaseFacade.getInstance();
			resultadoHash = hbf.getResults(tc, outputTable);
			hbf.dropTable(outputTable);
		} catch (PersistenciaException e) {
			e.printStackTrace();
			return new ArrayList<CBRCase>();
		}
		
		List<CBRCase> resultado = new ArrayList<CBRCase>(resultadoHash.size());
		for (HashMap<String,Serializable> caso : resultadoHash) {
			System.out.println("ID: "+caso.get("META_ID")+", age: "+caso.get("age")+", class: "+caso.get("class")+", sex: "+caso.get("sex")+", surv: "+caso.get("survived"));
			try {
				resultado.add(RellenadorClases.rellenarCaso(tc, caso));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// Hay que ejecutar otra vez el método de retrieval para combinar los resultados de los reducers
		Collection<CBRCase> result = EjecutorTecnicaRetrieval.ejecutarRetrieval(resultado, RellenadorClases.rellenarQuery(tc, query), tc);
		
		return result;
	}
	
	/**
	 * Borra el fichero del query serializado de HDFS.
	 * @param queryFile Nombre del fichero dentro de la carpeta "queries/".
	 * @param conf Configuración para crear el obejeto FileSystem.
	 */
	private void borraQueryHDFS(String queryFile, Configuration conf) {
		try{
			FileSystem fs = FileSystem.get(conf);
			Path outFile = new Path("queries/"+queryFile);
			if (fs.exists(outFile)) {
				fs.delete(outFile, false);
			}
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Escribe un tipo de caso en HDFS. Lo escribe en la carpeta "tcs/" dentro de la carpeta
	 * del usuario que ejecuta el servidor.
	 * @param tc El tipo de caso.
	 * @param conf Configuración para crear el objeto FileSystem.
	 */
	private void escribeTipoCasoHDFS (TipoCaso tc, Configuration conf) {
		try {
			FileSystem fs = FileSystem.get(conf);
			// "tcs/" se traduce en hdfs a "/user/$USER/tcs/"
			Path outFile = new Path("tcs/");
			if (!fs.exists(outFile)) {
				fs.mkdirs(outFile);
			}
			outFile = new Path(outFile, tc.getNombre()+".tc");
			escribeObjetoHDFS(tc, fs, outFile);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Escribe un CBRQuery en HDFS. Lo escribe en la carpeta "queries/" dentro de la carpeta
	 * del usuario que ejecuta el servidor, con un nombre determinado por el nombre del tipo
	 * de caso y un autonumérico.
	 * @param query El CBRQuery a escribir.
	 * @param tc El tipo de caso para obtener el nombre.
	 * @param conf Configuración para crear el objeto FileSystem.
	 * @return Nombre del fichero creado.
	 */
	private String escribeQueryHDFS (HashMap<String,Serializable> query, TipoCaso tc, Configuration conf) {
		String nombre = null;
		try {
			FileSystem fs = FileSystem.get(conf);
			// "queries/" se traduce en hdfs a "/user/$USER/queries/"
			Path outFolder = new Path("queries/");
			if (!fs.exists(outFolder)) {
				fs.mkdirs(outFolder);
			}
			Path outFile = null;
			int i = 1;
			do {
				nombre = tc.getNombre()+i+".query";
				outFile = new Path(outFolder, nombre);
				i++;
			} while (fs.exists(outFile));
			escribeObjetoHDFS(query, fs, outFile);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nombre;
	}

	/**
	 * Serializa un objeto en un fichero en HDFS.
	 * @param nombre Nombre del fichero.
	 * @param s Objeto a escribir.
	 * @param fs FileSystem que representa a HDFS.
	 * @param outFile Path que apunta al fichero a crear.
	 * @throws IOException
	 */
	private void escribeObjetoHDFS(Serializable s, FileSystem fs, Path outFile) throws IOException {
		FSDataOutputStream outfs = fs.create(outFile, true);
		ObjectOutputStream oos = new ObjectOutputStream(outfs);
		oos.writeObject(s);
		oos.flush();
		oos.close();
		outfs.close();
	}
}
