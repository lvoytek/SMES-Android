package com.smes.smes_android.ui.data;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smes_data.SecureData;
import com.smes.smes_android.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>
{
	public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		TextView name, location;
		CardView itemCard;

		public DataViewHolder(View itemView)
		{
			super(itemView);
			this.name = (TextView) itemView.findViewById(R.id.name);
			this.location = (TextView) itemView.findViewById(R.id.subtext);
			this.itemCard = (CardView) itemView.findViewById(R.id.item_card);
			itemView.setOnClickListener(this);
		}

		public void select()
		{
			this.itemCard.setCardBackgroundColor(this.itemCard.getResources().getColor(R.color.colorAccent));
		}

		public void deselect()
		{
			this.itemCard.setCardBackgroundColor(this.itemCard.getResources().getColor(R.color.deselected));
		}

		@Override
		public void onClick(View v)
		{
			if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
		}
	}

	private ArrayList<SecureData> dataArray;
	private HashMap<SecureData, DataViewHolder> vhMap;
	private SecureData selectedSD;
	private ItemClickListener mClickListener;

	public DataAdapter(ArrayList<SecureData> arrayData)
	{
		this.dataArray = arrayData;
		this.vhMap = new HashMap<>();
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

		this.vhMap.put(data, holder);
	}

	//Allows clicks events to be caught
	void setClickListener(ItemClickListener itemClickListener)
	{
		this.mClickListener = itemClickListener;
	}

	//Parent activity will implement this method to respond to click events
	public interface ItemClickListener
	{
		void onItemClick(View view, int position);
	}

	public void selectDataAt(int position)
	{
		if(this.selectedSD != null)
			this.vhMap.get(this.selectedSD).deselect();

		this.selectedSD = this.dataArray.get(position);
		if(this.selectedSD != null)
			this.vhMap.get(this.selectedSD).select();
	}

	public SecureData getSelectedSD()
	{
		return this.selectedSD;
	}
}
