package com.example.vivantsample;

import com.example.vivantsample.adapter.VivantListAdapter;
import com.example.vivantsample.data.GalleryInfo;
import com.example.vivantsample.data.OnRatingUpdate;
import com.example.vivantsample.data.SampleGalleryData;
import com.example.vivantsample.data.VivantDataStorage;
import com.example.vivantsample.data.VivantDataStorage.Record;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;

public class GalleryActivity extends Activity implements OnRatingUpdate {
	
	private ListView listView = null;
	private VivantDataStorage storage = null ;
	private int id ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		storage = new VivantDataStorage(getApplicationContext());
		
		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		
		VivantDataStorage.Record [] record = storage.readData(id);
		if (record == null) {
			System.out.println("I AM HERE: Record is null");
			record = createRecordToStore();
			storage.storeData(record);
		}
		
		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(new VivantListAdapter(this, record, this));
	}
	
	private VivantDataStorage.Record [] createRecordToStore() {
		String[][] data = new SampleGalleryData().galleryData;

		GalleryInfo galleryInfo = null;
		for (int i = 0;i < Global.galleries.galleries.size();i++) {
			galleryInfo = Global.galleries.galleries.get(i);
			if (id == galleryInfo.id)
				break;
		}
		
		VivantDataStorage.Record [] record = new VivantDataStorage.Record[galleryInfo.piecesofart];
		for (int i = 0;i < record.length;i++) {
			record[i] = new VivantDataStorage.Record();
			record[i].id = galleryInfo.id;
			record[i].firstLine = data[i][0];
			record[i].rating = "0.0" ;
		}
		
		return record;
		
	}

	@Override
	public void updateRecord(Record record) {
		storage.updateRecord(record);
	}
}
