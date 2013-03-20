package servidorcbr.persistencia.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;

public class HbaseFacade {

	private static HbaseFacade instance = null;
	private HConnection conn = null;
	private Configuration conf = null;

	private HbaseFacade() throws PersistenciaException {
		conf = HBaseConfiguration.create();
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

	public boolean createTable(TipoCaso tc) throws PersistenciaException {
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

	public boolean dropTable(TipoCaso tc) throws PersistenciaException {
		try {
			HBaseAdmin hba = new HBaseAdmin(conn);
			hba.disableTable(tc.getNombre());
			hba.deleteTable(tc.getNombre());
			hba.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return true;
	}

	public boolean putCasos(TipoCaso tc, List<HashMap<String, Object>> casos)
			throws PersistenciaException {
		try {
			HTable ht = new HTable(conf, tc.getNombre());
			long id = new Date().getTime();
			List<Put> puts = new ArrayList<Put>(casos.size());
			for (HashMap<String, Object> caso : casos) {
				Put p = new Put(Bytes.toBytes(id++));
				for (Map.Entry<String, Object> atb : caso.entrySet()) {
					Atributo a = tc.getAtbos().get(atb.getKey());
					addAtributo(p, atb, a);
				}
				puts.add(p);
			}
			ht.put(puts);
			ht.flushCommits();
			ht.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return true;
	}

	private void addAtributo(Put p, Map.Entry<String, Object> atb, Atributo a) {
		String familia;
		if (a.getEsProblema()) {
			familia = "problema";
		} else {
			familia = "solucion";
		}
		switch (a.getTipo()) {
		case "S":
			p.add(Bytes.toBytes(familia),
					Bytes.toBytes(a.getNombre()),
					Bytes.toBytes((String) atb.getValue()));
			break;
		case "I":
			p.add(Bytes.toBytes(familia),
					Bytes.toBytes(a.getNombre()),
					Bytes.toBytes((Integer) atb.getValue()));
			break;
		case "D":
			p.add(Bytes.toBytes(familia),
					Bytes.toBytes(a.getNombre()),
					Bytes.toBytes((Double) atb.getValue()));
		}
	}

}
