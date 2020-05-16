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

//Allows a list of SecureData entries to act as a formatted RecyclerView list
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>
{
	//Holds the list entry view associated with a SecureData entry
	public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		TextView name, location;
		CardView itemCard;

		//Keep track of the list_item card for coloring and the text items for setting viewable info
		public DataViewHolder(View itemView)
		{
			super(itemView);
			this.name = (TextView) itemView.findViewById(R.id.name);
			this.location = (TextView) itemView.findViewById(R.id.subtext);
			this.itemCard = (CardView) itemView.findViewById(R.id.item_card);
			itemView.setOnClickListener(this);
		}

		//Turn a SecureData entry view green to show that it is the data entry being actively edited
		public void select()
		{
			this.itemCard.setCardBackgroundColor(this.itemCard.getResources().getColor(R.color.colorAccent));
		}

		//Turn the SecureData entry view gray to show it is no longer active
		public void deselect()
		{
			this.itemCard.setCardBackgroundColor(this.itemCard.getResources().getColor(R.color.deselected));
		}

		//Notify a designated click listener that this specific item has been clicked
		@Override
		public void onClick(View v)
		{
			if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
		}
	}

	//The array of secure data to be adapted into a view
	private ArrayList<SecureData> dataArray;

	//Maps each SecureData item with its view for editing specific item style
	private HashMap<SecureData, DataViewHolder> vhMap;

	//A copy of the currently active SecureData entry
	private SecureData selectedSD;

	//listener of clicks on specific items
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

	//Use the list_item view as a template for the SecureData entry view
	@Override
	public DataAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
		return new DataAdapter.DataViewHolder(view);
	}

	//Bind a specific SecureData entry with its view item
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

	//Set the SecureData entry in a specific location as the active entry
	public void selectDataAt(int position)
	{
		if(this.selectedSD != null)
			this.vhMap.get(this.selectedSD).deselect();

		this.selectedSD = this.dataArray.get(position);
		if(this.selectedSD != null)
			this.vhMap.get(this.selectedSD).select();
	}

	//Get the currently selected SecureData entry
	public SecureData getSelectedSD()
	{
		return this.selectedSD;
	}
}
