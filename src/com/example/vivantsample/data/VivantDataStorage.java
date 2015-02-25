package com.example.vivantsample.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VivantDataStorage extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VivantDB.db";
    
	private String TABLE_NAME = "VivantTable";
	private String TABLE_COLUMN_ID = "id" ;
	private String TABLE_COLUMN_FIRSTLINE = "firstLine" ;
	private String TABLE_COLUMN_RATING = "rating";

	private String CREATE_TABLE_CMD = "CREATE TABLE " + TABLE_NAME + 
			" (" + TABLE_COLUMN_ID + " INTEGER," +
			TABLE_COLUMN_FIRSTLINE + " TEXT," +
			TABLE_COLUMN_RATING + " TEXT)";
	
	public VivantDataStorage(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static class Record {
		public int id;
		public String firstLine;
		public String rating;
	}
	
	public void updateRecord(VivantDataStorage.Record record) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(TABLE_COLUMN_ID, record.id);
		cv.put(TABLE_COLUMN_FIRSTLINE, record.firstLine);
		cv.put(TABLE_COLUMN_RATING, record.rating);
		
		String selection = TABLE_COLUMN_FIRSTLINE + " LIKE ? AND " + 
							TABLE_COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { record.firstLine, String.valueOf(record.id) };

		int updated = db.update(
		    TABLE_NAME,
		    cv,
		    selection,
		    selectionArgs);
	}
	
	public void storeData(VivantDataStorage.Record [] record) {
		SQLiteDatabase db = getWritableDatabase();
		
		for (int i = 0;i < record.length;i++) {
			ContentValues cv = new ContentValues();
			cv.put(TABLE_COLUMN_ID, record[i].id);
			cv.put(TABLE_COLUMN_FIRSTLINE, record[i].firstLine);
			cv.put(TABLE_COLUMN_RATING, record[i].rating);
			
			db.insert(TABLE_NAME, null, cv);
		}
	}
	
	public void truncateTable() {
		SQLiteDatabase db = getWritableDatabase();
		
		db.delete(TABLE_NAME, null, null);
	}
	
	public VivantDataStorage.Record [] readData(int id) {
		String [] projection = {
			TABLE_COLUMN_ID,
			TABLE_COLUMN_FIRSTLINE,
			TABLE_COLUMN_RATING
		};
		
		String selection = TABLE_COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME,
						projection, 
						selection, 
						selectionArgs, 
						null, 
						null, 
						null) ;
		
		if (cursor.getCount() <= 0)
			return null;
		
		VivantDataStorage.Record [] record = new VivantDataStorage.Record[cursor.getCount()];
		
		cursor.moveToFirst();
		
		int i = 0;
		do {
			record[i] = new VivantDataStorage.Record();
			record[i].id = cursor.getInt(0);
			record[i].firstLine = cursor.getString(1);
			record[i].rating = cursor.getString(2);
			i++;
		} while(cursor.moveToNext());
		
		return record;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
