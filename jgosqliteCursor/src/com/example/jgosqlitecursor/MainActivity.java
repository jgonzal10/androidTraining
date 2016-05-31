package com.example.jgosqlitecursor;


import java.util.ArrayList;
import java.util.List;


import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity{
	
	private List developersList;
	
	//DB variables
	private final String dbName = "developersDB";
	private static SQLiteDatabase sqliteDB = null;
	private ArrayAdapter myAdapter;
	
	private final String tableName = "topteam";
	
	//developers array
	private final String[] developerName = new String[] {"jgo", "mark","jobs","larissa"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		developersList = new ArrayList();
		myAdapter = new ArrayAdapter(this, R.layout.row_layout, R.id.listText,developersList);
		
		
		//declare sql data object first
		sqliteDB = null;
		
		try{
			sqliteDB =  this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
			//executing my sqlite query
			sqliteDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (developerName VARCHAR);");
			//inserting names into table
			for (String ver : developerName) {
				sqliteDB.execSQL("INSERT INTO " + tableName + " Values ('" + ver + "');");
			}
			
			Cursor cursor = sqliteDB.rawQuery("SELECT developerName FROM " + tableName, null);
			if(cursor!=null){
				if(cursor.moveToFirst()){
					do{
						String developer = cursor.getString(cursor.getColumnIndex("developerName"));
						developersList.add(developer);
					}while(cursor.moveToNext());
				}
			}
			
			//initialize the adapter
			myAdapter=new ArrayAdaper(this, R.layout.row_layout, R.id.listText, developersList);
			setListAdapter(myAdapter);
			
			
			
		}catch(SQLiteException se){
			Toast.makeText(getApplicationContext(), "Error creating the database", Toast.LENGTH_LONG).show();
		}finally {
			if (sqliteDB != null) {
				sqliteDB.execSQL("DELETE FROM " + tableName);
				sqliteDB.close();
			}
		}
	}


}
