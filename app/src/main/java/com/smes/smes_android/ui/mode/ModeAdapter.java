package com.smes.smes_android.ui.mode;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smes_mode.Mode;
import com.smes.smes_android.R;
import com.smes.tinkerboard_gpio.sensors.Sensor;

import java.util.ArrayList;

public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.ModeViewHolder>
{
	public class ModeViewHolder extends RecyclerView.ViewHolder
	{
		TextView name, sensorType;

		public ModeViewHolder(View itemView)
		{
			super(itemView);
			this.name = (TextView) itemView.findViewById(R.id.name);
			this.sensorType = (TextView) itemView.findViewById(R.id.subtext);
		}
	}

	private ArrayList<Mode> mArrayModes;

	public ModeAdapter(ArrayList<Mode> arrayModes)
	{
		this.mArrayModes = arrayModes;
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
	}
}
