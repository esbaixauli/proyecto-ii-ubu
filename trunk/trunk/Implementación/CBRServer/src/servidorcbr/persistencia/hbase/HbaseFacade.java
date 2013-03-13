package servidorcbr.persistencia.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;

import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;

public class HbaseFacade {

	private static HbaseFacade instance = null;
	private HConnection conn = null;
	
	private HbaseFacade () throws PersistenciaException {
		Configuration conf = HBaseConfiguration.create();
		try {
			conn = HConnectionManager.createConnection(conf);
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
	}
	
	public static HbaseFacade getInstance() throws PersistenciaException {
		if (instance == null) {
			instance = new HbaseFacade();
		}
		return instance;
	}
	
	public boolean createTable (TipoCaso tc) throws PersistenciaException {
		HTableDescriptor ht = new HTableDescriptor(tc.getNombre());
		ht.addFamily(new HColumnDescriptor("problema"));
		ht.addFamily(new HColumnDescriptor("solucion"));
		try {
			HBaseAdmin hba = new HBaseAdmin(conn);
			hba.createTable(ht);
			hba.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return true;
	}
	
}
