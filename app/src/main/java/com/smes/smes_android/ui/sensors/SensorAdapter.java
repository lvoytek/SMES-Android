package com.smes.smes_android.ui.sensors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smes.smes_android.R;
import com.smes.tinkerboard_gpio.sensors.Sensor;

import java.util.ArrayList;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder>
{
	public class SensorViewHolder extends RecyclerView.ViewHolder
	{
		TextView name, sensorType;

		public SensorViewHolder(View itemView)
		{
			super(itemView);
			this.name = (TextView) itemView.findViewById(R.id.name);
			this.sensorType = (TextView) itemView.findViewById(R.id.subtext);
		}
	}

	private ArrayList<Sensor> mArraySensors;

	public SensorAdapter(ArrayList<Sensor> arraySensors)
	{
		this.mArraySensors = arraySensors;
	}

	@Override
	public int getItemCount()
	{
		return this.mArraySensors.size();
	}

	@Override
	public SensorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
		return new SensorViewHolder(view);
	}

	@Override
	public void onBindViewHolder(SensorViewHolder holder, int position)
	{
		Sensor sensor = this.mArraySensors.get(position);

		holder.name.setText(sensor.toString());
		holder.sensorType.setText(sensor.getSensorType());
	}
}
