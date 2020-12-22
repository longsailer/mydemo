package org.ray.leveldb.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.Options;

/**
 * LevelDBServer.java
 * <br><br>
 * @author: ray
 * @date: 2020年11月30日
 */
public class LevelDBServer {
	
	private DB db;
	
	public LevelDBServer() throws IOException{
		Options options = new Options();
        options.createIfMissing(true);
        this.db = JniDBFactory.factory.open(new File("/temp/lvltest"), options);
	}
	
	
	public void put(String key, String value) throws DBException, UnsupportedEncodingException{
		this.db.put(key.getBytes("UTF-8"), value.getBytes("UTF-8"));
	}
	
	public String get(String key) throws DBException, UnsupportedEncodingException{
		byte[] v = this.db.get(key.getBytes("UTF-8"));
		return new String(v);
	}
}

