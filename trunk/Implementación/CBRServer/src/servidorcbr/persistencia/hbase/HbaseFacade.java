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

/**
 * Fachada de acceso a la capa de persistencia en Hbase. Sigue el patrón de diseño Singleton.
 * @author Rubén Antón García, Enrique Sainz Baixauli
 *
 */
/**
 * @author quique
 *
 */
public class HbaseFacade {

	/**
	 * Instancia estática de la propia clase. Patrón de diseño Singleton.
	 */
	private static HbaseFacade instance = null;
	
	/**
	 * Conexión con Hbase.
	 */
	private HConnection conn = null;
	
	/**
	 * Configuración de la conexión con Hbase.
	 */
	private Configuration conf = null;
	
	/**
	 * ColumnFamily en la que se almacenan todos los campos. Los campos no se separan en 
	 * distintas ColumnFamily porque distintas ColumnFamily pueden almacenarse en distintas
	 * regiones de la base de datos distribuida. Es necesario que todos los campos estén en
	 * la misma región para mantener la eficiencia de la clase ReducerRetrieval.
	 * @see servidorcbr.controlador.cicloCBR.ReducerRetrieval 
	 */
	private final byte[] cf = Bytes.toBytes("campos");

	/**
	 * Constructor privado para mantener el patrón Singleton. Crea la configuración de la
	 * conexión y la conexión en sí.
	 * @throws PersistenciaException Si hay un error de conexión.
	 */
	private HbaseFacade() throws PersistenciaException {
		conf = HBaseConfiguration.create();
		try {
			conn = HConnectionManager.createConnection(conf);
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
	}

	/**
	 * Método estático que provee acceso a la única instancia de esta clase. Además, comprueba
	 * cada vez que la conexión sigue viva y si está cerrada crea una nueva.
	 * @return La instancia única de esta clase.
	 * @throws PersistenciaException Si hay algún error de conexión.
	 */
	public static HbaseFacade getInstance() throws PersistenciaException {
		if (instance == null || instance.conn.isClosed()) {
			instance = new HbaseFacade();
		}
		return instance;
	}

	/**
	 * Crea una tabla en Hbase con el nombre del tipo de caso.
	 * @param tc El tipo de caso para el que se crea la tabla.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public boolean createTable(TipoCaso tc) throws PersistenciaException {
		return createTable(tc.getNombre());
	}
	
	/**
	 * Crea una tabla en Hbase con un nombre dado. Este método es utilizado por LanzadorCBR
	 * para crear la tabla temporal de resultados de los distintos ReducerRetrieval.
	 * @param nombre Nombre de la tabla a crear.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
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
	
	/**
	 * Elimina la tabla que representa un tipo de caso de Hbase.
	 * @param tc El tipo de caso cuya tabla hay que borrar.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
	public boolean dropTable(TipoCaso tc) throws PersistenciaException {
		return dropTable(tc.getNombre());
	}

	/**
	 * Elimina una tabla de Hbase dado su nombre.
	 * @param nombre El nombre de la tabla a eliminar.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
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

	/**
	 * Inserta una serie de casos en la tabla correspondiente a su tipo de caso en Hbase.
	 * @param tc El tipo de caso que define la estructura de los casos.
	 * @param casos Los casos concretos a insertar.
	 * @return Si la operación tuvo éxito o no.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
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
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return true;
	}

	/**
	 * Añade el valor de un atributo a un objeto de tipo Put para insertarlo en la tabla de
	 * Hbase.
	 * @param p El objeto de tipo Put al que se le añade el valor del atributo.
	 * @param atb El Map.Entry que contiene el valor del atributo.
	 * @param a El atributo que define el tipo de datos correspondiente.
	 */
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
	
	/**
	 * Devuelve el identificador de la región de Hbase que contiene una determinada fila de
	 * una determinada tabla. 
	 * @param tableName El nombre de la tabla, como array de bytes.
	 * @param row El id de la fila, como array de bytes.
	 * @return El identificador de la región.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
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
	
	/**
	 * Devuelve el número de regiones en las que está distribuida una determinada tabla.
	 * @param tableName El nombre de la tabla, como array de bytes.
	 * @return El número de regiones en las que se divide.
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
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
	
	/**
	 * Devuelve los resultados de una operación MapReduce, que han sido almacenados en una
	 * tabla de Hbase.
	 * @param tc El tipo de caso, que define la estructura del caso.
	 * @param tableName El nombre de la tabla que contiene los resultados.
	 * @return Una lista que contiene los resultados de la operación MapReduce, en forma de
	 * HashMaps que mapean "nombre del atributo" -> "valor".
	 * @throws PersistenciaException Si se produjo algún error en la operación.
	 */
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
		    	h.put("META_QUALITY", Bytes.toInt(r.getValue(cf, Bytes.toBytes("META_QUALITY"))));
		    	lista.add(h);
		    }
		    table.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new PersistenciaException(e);
		}
		return lista;
	}

}
