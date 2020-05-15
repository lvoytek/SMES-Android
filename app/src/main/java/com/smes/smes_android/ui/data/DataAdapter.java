package com.smes.smes_android.ui.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smes_data.SecureData;
import com.smes.smes_android.R;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>
{
	public class DataViewHolder extends RecyclerView.ViewHolder
	{
		TextView name, location;

		public DataViewHolder(View itemView)
		{
			super(itemView);
			this.name = (TextView) itemView.findViewById(R.id.name);
			this.location = (TextView) itemView.findViewById(R.id.subtext);
		}
	}

	private ArrayList<SecureData> dataArray;

	public DataAdapter(ArrayList<SecureData> arrayData)
	{
		this.dataArray = arrayData;
	}

	@Override
	public int getItemCount()
	{
		return this.dataArray.size();
	}

	@Override
	public DataAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
		return new DataAdapter.DataViewHolder(view);
	}

	@Override
	public void onBindViewHolder(DataAdapter.DataViewHolder holder, int position)
	{
		SecureData data = this.dataArray.get(position);

		holder.name.setText(data.toString());
		holder.location.setText(data.getFileName());
	}
}
