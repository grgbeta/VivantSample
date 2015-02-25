package com.example.vivantsample.adapter;

import com.example.vivantsample.Global;
import com.example.vivantsample.R;
import com.example.vivantsample.RateActivity;
import com.example.vivantsample.data.GalleryInfo;
import com.example.vivantsample.data.OnRatingUpdate;
import com.example.vivantsample.data.SampleGalleryData;
import com.example.vivantsample.data.VivantDataStorage;
import com.example.vivantsample.data.VivantDataStorage.Record;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class VivantListAdapter extends BaseAdapter implements OnClickListener {
	//private GalleryInfo galleryInfo = null ;
	private Context context = null;
	private VivantDataStorage.Record [] record = null;
	private OnRatingUpdate onRatingUpdate = null;
	private float latestRating ;
	
	public VivantListAdapter(Context context, VivantDataStorage.Record [] record, OnRatingUpdate onRatingUpdate) {
		this.context = context ;
		//this.galleryInfo = Global.galleries.galleries.get(id);
		this.record = record;
		this.onRatingUpdate = onRatingUpdate;
		
		
	}

	@Override
	public int getCount() {
		return record.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.gallery_list_item, parent, false);
		
		TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
		TextView secondLine = (TextView) rowView.findViewById(R.id.ratingBar);
		ImageView image = (ImageView) rowView.findViewById(R.id.icon);
		
		firstLine.setText(record[position].firstLine);
		secondLine.setText("Rating: " + record[position].rating);
		image.setBackgroundResource(R.drawable.ic_launcher);
		
		rowView.setTag(record[position]);
		
		rowView.setOnClickListener(this);


		return rowView;
	}

	@Override
	public void onClick(final View v1) {
		final Dialog rankDialog = new Dialog(context, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rating_alert);
        rankDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        
        final VivantDataStorage.Record viewRec = (VivantDataStorage.Record) v1.getTag();
        text.setText(viewRec.firstLine);
        ratingBar.setRating(Float.valueOf(viewRec.rating));
        latestRating = Float.valueOf(viewRec.rating);
        
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				latestRating = rating;
				
			}
			
		});

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
            	
                rankDialog.dismiss();
                //TextView secondLine = (TextView) v1.findViewById(R.id.dialog_ratingbar);
                
                viewRec.rating = String.valueOf(latestRating);
        		//secondLine.setText("Rating: " + viewRec.rating);

                onRatingUpdate.updateRecord(viewRec);
                
                notifyDataSetChanged();
            }
        });

        rankDialog.show();
	}

}
