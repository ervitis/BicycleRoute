package com.pack.bicycleroute;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteAdapter extends SQLiteOpenHelper{
	private static String DATABASE_NAME = "recorridos";
	private static String TABLE_RECORRIDOS = "recorrido";
	private static String TABLE_FECHARECORRIDOS = "fecha_recorridos";
	private static int VERSION = 1;
	
	/**
	 * Constructor for sqlite adapter
	 * @param context	the context
	 */
	public SQLiteAdapter(Context context){
		super(context, DATABASE_NAME, null, VERSION);
	}

	/**
	 * onCreate the database
	 */
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase) {
		try{
			sqliteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" + TABLE_RECORRIDOS + "(" +
					"_id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
					"horaInicio INTEGER NOT NULL, " +
					"minInicio INTEGER NOT NULL," +
					"horaFin INTEGER NOT NULL, " +
					"minFin INTEGER NOT NULL, " +
					"distancia DOUBLE NOT NULL);");
			sqliteDatabase.execSQL("CREATE TABLE IF NOT EXISTS" + TABLE_FECHARECORRIDOS + "(" + 
					"_id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
					"dia INTEGER NOT NULL," + 
					"mes INTEGER NOT NULL," +
					"anho INTEGER NOT NULL," +
					"_idR INTEGER NOT NULL," +
					"FOREIGN KEY (_idR) REFERENCES " + TABLE_RECORRIDOS + "(_id) ON UPDATE CASCADE ON DELETE CASCADE);");
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * onUpgrade the database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldVersion, int newVersion) {
		sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FECHARECORRIDOS);
		sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORRIDOS);
		onCreate(sqliteDatabase);
	}
	
	/**
	 * Saves the data and returns the last id for the date
	 * @param horaInicio	Start hour
	 * @param minInicio		Start minute
	 * @param horaFin		Finish hour
	 * @param minFin		Finish minute
	 * @param distancia		Distance
	 * @return				The last id used or <code>-1</code> if an exception has been thrown
	 */
	public int guardaRecorrido(int horaInicio, int minInicio, int horaFin, int minFin, double distancia){
		Cursor cursor;
		int id = -1;
		
		try{
			SQLiteDatabase db = getWritableDatabase();
			
			db.execSQL("INSERT INTO " + TABLE_RECORRIDOS + " (horaInicio, minInicio, horaFin, minFin, distancia) VALUES ( " +
					horaInicio + ", " + minInicio + ", " + horaFin + ", " + minFin + ", " + distancia + ");");
			
			//get the last id
			cursor = db.rawQuery("SELECT MAX(_id) FROM " + TABLE_RECORRIDOS + ";", null);
			
			while ( cursor.moveToNext() ){
				id = cursor.getInt(0);
			}
			
			cursor.close();
						
			return id;
		}catch(Exception ex){
			return -1;
		}
	}
		
	/**
	 * Saves the date
	 * @param id		the id
	 * @param dia		day
	 * @param mes		month
	 * @param anho		year
	 * @return			<code>0</code> no error. <code>1</code> there was an error.
	 */
	public int guardaFecha(int id, int dia, int mes, int anho){
		try{
			SQLiteDatabase db = getWritableDatabase();
			
			db.execSQL("INSERT INTO " + TABLE_FECHARECORRIDOS + " (dia, mes, anho, _idR) VALUES (" +
					dia + ", " + mes + ", " + anho + ", " + id + ");");
			
			return 0;
		}catch(Exception ex){
			return -1;
		}
	}
}
