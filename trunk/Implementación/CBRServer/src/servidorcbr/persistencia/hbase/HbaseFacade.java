package servidorcbr.persistencia.hbase;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import servidorcbr.modelo.Atributo;
import servidorcbr.modelo.TipoCaso;
import servidorcbr.modelo.excepciones.PersistenciaException;

public class HbaseFacade {

	private static HbaseFacade instance = null;
	private HConnection conn = null;
	private Configuration conf = null;
	private final byte[] cf = Bytes.toBytes("campos");

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
		if (instance == null || instance.conn.isClosed()) {
			instance = new HbaseFacade();
		}
		return instance;
	}

	public boolean createTable(TipoCaso tc) throws PersistenciaException {
		return createTable(tc.getNombre());
	}
	
	public boolean createTable(String nombre) throws PersistenciaException {
		HTableDescriptor ht = new HTableDescriptor(nombre);
		ht.addFamily(new HColumnDescriptor(cf));
		try {
			HBaseAdmin hba = new HBaseAdmin(conn);
			hba.createTable(ht);
			hba.close();
		} catch (IllegalArgumentException e) {
			return false;
		} catch (TableExistsException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return true;
	}
	
	public boolean dropTable(TipoCaso tc) throws PersistenciaException {
		return dropTable(tc.getNombre());
	}

	public boolean dropTable(String nombre) throws PersistenciaException {
		try {
			HBaseAdmin hba = new HBaseAdmin(conn);
			hba.disableTable(nombre);
			hba.deleteTable(nombre);
			hba.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return true;
	}

	public boolean putCasos(TipoCaso tc, List<HashMap<String, Serializable>> casos)
			throws PersistenciaException {
		try {
			HTable ht = new HTable(conf, tc.getNombre());
			long id = new Date().getTime();
			List<Put> puts = new ArrayList<Put>(casos.size());
			for (HashMap<String, Serializable> caso : casos) {
				Put p = new Put(Bytes.toBytes(id++));
				for (Map.Entry<String, Serializable> par : caso.entrySet()) {
					if (par.getKey().equals("META_QUALITY")) {
						p.add(cf, 
								Bytes.toBytes(par.getKey()), 
								Bytes.toBytes((Integer) par.getValue()));
					} else {
						Atributo a = tc.getAtbos().get(par.getKey());
						addAtributo(p, par, a);
					}
				}
				puts.add(p);
			}
			ht.put(puts);
			ht.flushCommits();
			ht.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return true;
	}

	private void addAtributo(Put p, Map.Entry<String, Serializable> atb, Atributo a) {
		switch (a.getTipo()) {
		case "S":
			p.add(cf,
					Bytes.toBytes(a.getNombre()),
					Bytes.toBytes((String) atb.getValue()));
			break;
		case "I":
			p.add(cf,
					Bytes.toBytes(a.getNombre()),
					Bytes.toBytes((Integer) atb.getValue()));
			break;
		case "D":
			p.add(cf,
					Bytes.toBytes(a.getNombre()),
					Bytes.toBytes((Double) atb.getValue()));
		}
	}
	
	public long getRegionId (byte[] tableName, byte[] row) throws PersistenciaException {
		try {
			HRegionLocation loc = conn.locateRegion(tableName, row);
			if (loc == null) {
				return 0;
			}
			HRegionInfo inf = loc.getRegionInfo();
			if (inf == null) {
				return 0;
			}
			return inf.getRegionId();
		} catch (IOException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
	}
	
	public int getNRegions (byte[] tableName) throws PersistenciaException {
		try {
			List<HRegionLocation> l = conn.locateRegions(tableName);
			if (l == null) {
				return 0;
			} else {
				return l.size();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
	}
	
	public List<HashMap<String,Serializable>> getResults(TipoCaso tc, String tableName) throws PersistenciaException {
		List<HashMap<String,Serializable>> lista = new ArrayList<HashMap<String,Serializable>>();
		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			s.addFamily(cf);
		    ResultScanner scanner = table.getScanner(s);
		    for (Result r : scanner) {
		    	HashMap<String,Serializable> h = new HashMap<String,Serializable>();
		    	for (Atributo a : tc.getAtbos().values()) {
		    		switch (a.getTipo()) {
		    		case "I":
		    			h.put(a.getNombre(), Bytes.toInt(r.getValue(cf, Bytes.toBytes(a.getNombre()))));
		    			break;
		    		case "D":
		    			h.put(a.getNombre(), Bytes.toDouble(r.getValue(cf, Bytes.toBytes(a.getNombre()))));
		    			break;
		    		case "S":
		    			h.put(a.getNombre(), Bytes.toString(r.getValue(cf, Bytes.toBytes(a.getNombre()))));
		    			break;
		    		}
		    	}
		    	//h.put("META_ID", Bytes.toLong(r.getValue(cf, Bytes.toBytes("META_ID"))));
		    	h.put("META_ID", 0);
		    	lista.add(h);
		    }
		} catch (IOException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return lista;
	}

}
