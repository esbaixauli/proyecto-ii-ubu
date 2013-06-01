package servidorcbr.controlador.cicloCBR;

import java.io.IOException;

import jcolibri.cbrcore.CBRCase;

import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.IdentityTableMapper;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Mapper;

import servidorcbr.modelo.excepciones.PersistenciaException;
import servidorcbr.persistencia.hbase.HbaseFacade;

/**
 * Mapper utilizado en la etapa de recuperación. A cada fila (caso) almacenada en Hbase, le 
 * asigna como clave la región de Hbase en la que se encuentra. De esta forma, cada Reducer
 * procesará los casos que se encuentran almacenados en su región.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
public class MapperRetrieval extends TableMapper<ImmutableBytesWritable, Result> {

	/* (non-javadoc)
	 * @see org.apache.hadoop.mapreduce.Mapper#map(KEYIN, VALUEIN, org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	public void map(ImmutableBytesWritable key, Result value, Mapper.Context context)
					throws IOException, InterruptedException {
		byte[] tableName = Bytes.toBytes(context.getConfiguration().get("tipocaso"));
		byte[] row = key.copyBytes();
		long regionId = 0;
		try {
			regionId = HbaseFacade.getInstance().getRegionId(tableName, row);
		} catch (PersistenciaException e) {
			e.printStackTrace();
		}
		byte[] region = Bytes.toBytes(regionId);
		context.write(new ImmutableBytesWritable(region), value);
	}
}
