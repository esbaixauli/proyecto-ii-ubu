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

public class MapperRetrieval extends TableMapper<ImmutableBytesWritable, Result> {

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
		System.out.println("## Mapper ejecutandose: region "+regionId);
		byte[] region = Bytes.toBytes(regionId);
		context.write(new ImmutableBytesWritable(region), value);
	}
}
