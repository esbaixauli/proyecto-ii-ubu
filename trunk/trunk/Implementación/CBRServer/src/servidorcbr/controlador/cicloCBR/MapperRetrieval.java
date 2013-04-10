package servidorcbr.controlador.cicloCBR;

import java.io.IOException;

import jcolibri.cbrcore.CBRCase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.IdentityTableMapper;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.mapreduce.Mapper;

//public class MapperRetrieval extends Mapper<ImmutableBytesWritable, TableInputFormat, ImmutableBytesWritable, TableOutputFormat> {
public class MapperRetrieval extends IdentityTableMapper {

	@Override
	public void map(ImmutableBytesWritable key, Result value, org.apache.hadoop.mapreduce.Mapper.Context context) throws IOException, InterruptedException {
	//public void map(ImmutableBytesWritable key, TableInputFormat value, org.apache.hadoop.mapreduce.Mapper.Context context) throws IOException, InterruptedException {
		super.map(key, value, context);
		System.out.println("## Mapper ejecutandose");
	}
}
