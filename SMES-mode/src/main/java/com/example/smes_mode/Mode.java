package com.example.smes_mode;

import java.util.HashMap;

import com.example.smes_data.SecureData;
import com.smes.tinkerboard_gpio.sensors.Sensor;

public class Mode
{
	protected String name;
	protected HashMap<String, Sensor> availableSensors;
	protected HashMap<String, SecureData> availableData;

	protected boolean canCreateModes;
	protected boolean canAddSensors;
	protected boolean canAddData;
	protected boolean readOnly;

	public Mode(String name, boolean canCreateModes, boolean canAddSensors, boolean canAddData, boolean readOnly)
	{
		this.name = name;
		this.availableSensors = new HashMap<>();
		this.availableData = new HashMap<>();
		this.canCreateModes = canCreateModes;
		this.canAddSensors = canAddSensors;
		this.canAddData = canAddData;
		this.readOnly = readOnly;
	}

	public Mode(String name)
	{
		this(name, false, false, false, true);
	}


	public void addSensorAccess(Sensor sensor)
	{
		this.availableSensors.put(sensor.toString(), sensor);
	}

	public void removeSensorAccess(Sensor sensor)
	{
		if(this.availableSensors.containsKey(sensor.toString()))
		{
			this.availableSensors.remove(sensor.toString());
		}
	}

	public void removeSensorAccess(String sensorName)
	{
		if(this.availableSensors.containsKey(sensorName))
		{
			this.availableSensors.remove(sensorName);
		}
	}

	public void addDataAccess(SecureData data)
	{
		this.availableData.put(data.toString(), data);
	}

	public void removeDataAccess(SecureData data)
	{
		if(this.availableData.containsKey(data.toString()))
		{
			this.availableData.remove(data.toString());
		}
	}

	public void removeDataAccess(String dataName)
	{
		if(this.availableData.containsKey(dataName))
		{
			this.availableData.remove(dataName);
		}
	}

	public boolean hasAccess(SecureData data)
	{
		return this.availableData.containsKey(data.toString());
	}

	public boolean hasAccess(Sensor sensor)
	{
		return this.availableSensors.containsKey(sensor.toString());
	}


	public boolean creatingModesOK()
	{
		return this.canCreateModes;
	}

	public boolean creatingSensorsOK()
	{
		return this.canAddSensors;
	}

	public boolean creatingDataOK()
	{
		return this.canAddData;
	}

	public boolean isReadOnly()
	{
		return this.readOnly;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
