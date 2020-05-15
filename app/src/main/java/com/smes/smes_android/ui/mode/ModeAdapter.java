package com.smes.smes_android.ui.mode;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smes_mode.Mode;
import com.smes.smes_android.R;
import com.smes.tinkerboard_gpio.sensors.Sensor;

import java.util.ArrayList;
import java.util.HashMap;

public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.ModeViewHolder>
{
	public class ModeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		TextView name, sensorType;
		CardView itemCard;

		public ModeViewHolder(View itemView)
		{
			super(itemView);
			this.name = (TextView) itemView.findViewById(R.id.name);
			this.sensorType = (TextView) itemView.findViewById(R.id.subtext);
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
		public void onClick(View view)
		{
			if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
		}
	}

	private ArrayList<Mode> mArrayModes;
	private HashMap<Mode, ModeViewHolder> vhMap;
	private Mode currentMode;
	private ItemClickListener mClickListener;

	public ModeAdapter(ArrayList<Mode> arrayModes)
	{
		this.mArrayModes = arrayModes;
		this.vhMap = new HashMap<>();
	}

	@Override
	public int getItemCount()
	{
		return this.mArrayModes.size();
	}

	@Override
	public ModeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
		return new ModeViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ModeViewHolder holder, int position)
	{
		Mode mode = this.mArrayModes.get(position);

		holder.name.setText(mode.toString());
		holder.sensorType.setText(mode.getSubtext());

		this.vhMap.put(mode, holder);

		if (this.getItemCount() == 1)
			this.currentMode = mode;

		if(mode == this.currentMode)
			selectModeAt(position);


	}

	public Mode getModeAt(int position)
	{
		return this.mArrayModes.get(position);
	}

	public ModeViewHolder getModeVHAt(int position)
	{
		return this.vhMap.get(this.getModeAt(position));
	}

	public ModeViewHolder getModeVHFromMode(Mode mode)
	{
		return this.vhMap.get(mode);
	}

	public void selectModeAt(int position)
	{
		if(this.currentMode != null)
			this.vhMap.get(this.currentMode).deselect();
		this.currentMode = this.getModeAt(position);
		this.vhMap.get(this.currentMode).select();
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

	public Mode getCurrentMode()
	{
		return this.currentMode;
	}

	public void setCurrentMode(Mode mode)
	{
		int pos = this.mArrayModes.indexOf(mode);

		if (pos > -1)
			this.currentMode = mode;
	}

	public Mode getModeFromName(String modeName)
	{
		for(Mode m : this.mArrayModes)
		{
			if (m.toString().equals(modeName))
				return m;
		}
		return null;
	}
}
