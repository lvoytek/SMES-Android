package com.smes.smes_android.ui.sensors;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.example.smes_mode.Mode;
import com.smes.smes_android.R;
import com.smes.tinkerboard_gpio.GPIOPin;
import com.smes.tinkerboard_gpio.sensors.KeyPad;
import com.smes.tinkerboard_gpio.sensors.PressureSensor;
import com.smes.tinkerboard_gpio.sensors.Sensor;

import java.util.ArrayList;

public class SensorsFragment extends Fragment
{

	private SensorsViewModel sensorsViewModel;
	private GPIOPin pin185;
	private KeyPad pad;
	private PressureSensor press;
	private ArrayList<Sensor> sensors;

	private Mode currentMode;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		sensors = new ArrayList<Sensor>();
		sensorsViewModel = ViewModelProviders.of(this).get(SensorsViewModel.class);
		View root = inflater.inflate(R.layout.fragment_sensors, container, false);

		pin185 = new GPIOPin(185);
		pin185.setDirection(true);
		pad = new KeyPad("KP", 251, 255, 171, 163, 162, 184, 160, 161);
		sensors.add(pad);

		press = new PressureSensor("PS", 188);
		sensors.add(press);


		Button button = (Button) root.findViewById(R.id.off_button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pin185.setOutput(false);
			}
		});

		button = (Button) root.findViewById(R.id.on_button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pin185.setOutput(true);
			}
		});

		FloatingActionButton floatButton = (FloatingActionButton) root.findViewById(R.id.new_item_button);
		final CardView newSensorCard = ((CardView) root.findViewById(R.id.new_sensor_card));
		floatButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				newSensorCard.setVisibility(View.VISIBLE);
			}
		});



		button = (Button) root.findViewById(R.id.test_root_button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});

		final Spinner sensorSpinner = (Spinner) root.findViewById(R.id.sensor_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.sensors_array, R.layout.support_simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		sensorSpinner.setAdapter(adapter);
		sensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				switch((String) parent.getItemAtPosition(position))
				{
					case "KeyPad":

						break;
					default:


				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		RecyclerView sensorList = (RecyclerView) root.findViewById(R.id.sensor_list);
		sensorList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		final SensorAdapter sensorAdapter = new SensorAdapter(sensors);
		sensorList.setAdapter(sensorAdapter);


		final EditText sensorName = (EditText) root.findViewById(R.id.new_sensor_name);
		button = (Button) root.findViewById(R.id.add_sensor);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				newSensorCard.setVisibility(View.INVISIBLE);

				boolean sensorAdded = true;
				switch ((String) sensorSpinner.getSelectedItem())
				{
					case "KeyPad":
						sensors.add(new KeyPad(sensorName.getText().toString(), 1, 1, 1, 1, 1, 1, 1, 1));
						break;
					case "Pressure Sensor":
						sensors.add(new PressureSensor(sensorName.getText().toString(), 1));
						break;
					default:
						sensorAdded = false;
						break;

				}

				if(sensorAdded)
					sensorAdapter.notifyItemInserted(sensors.size() - 1);
			}
		});

		return root;
	}

	public void updateCurrentMode(Mode mode)
	{
		this.currentMode = mode;
	}
}
