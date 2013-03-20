package servidorcbr.controlador.cicloCBR;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import servidorcbr.controlador.cicloCBR.ejecucion.EjecutorTecnicaRetrieval;

public class ReducerRetrieval
		extends
		Reducer<ImmutableBytesWritable, IntWritable, ImmutableBytesWritable, IntWritable> {
	private EjecutorTecnicaRetrieval et;
	
	public ReducerRetrieval(EjecutorTecnicaRetrieval et){
		super();
		this.et=et;
	}
	
	@Override
	public void reduce(ImmutableBytesWritable key, Iterable<IntWritable> values, Context context){
		//Convierte los values a objetos del problema y se los manda a ejecutar
		
	}

}
