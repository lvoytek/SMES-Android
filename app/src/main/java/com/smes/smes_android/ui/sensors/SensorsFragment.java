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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.example.smes_mode.Mode;
import com.smes.smes_android.R;
import com.smes.tinkerboard_gpio.GPIOPin;
import com.smes.tinkerboard_gpio.sensors.KeyPad;
import com.smes.tinkerboard_gpio.sensors.ButtonSensor;
import com.smes.tinkerboard_gpio.sensors.PulseOximeter;
import com.smes.tinkerboard_gpio.sensors.Sensor;

import java.util.ArrayList;

public class SensorsFragment extends Fragment
{

	private SensorsViewModel sensorsViewModel;
	private GPIOPin pin185;
	private KeyPad pad;
	private ButtonSensor press;
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

		press = new ButtonSensor("PS", 188);
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


		final Spinner[] gpioSpinners = new Spinner[8];
		gpioSpinners[0] = (Spinner) root.findViewById(R.id.new_gpio_0);
		gpioSpinners[1] = (Spinner) root.findViewById(R.id.new_gpio_1);
		gpioSpinners[2] = (Spinner) root.findViewById(R.id.new_gpio_2);
		gpioSpinners[3] = (Spinner) root.findViewById(R.id.new_gpio_3);
		gpioSpinners[4] = (Spinner) root.findViewById(R.id.new_gpio_4);
		gpioSpinners[5] = (Spinner) root.findViewById(R.id.new_gpio_5);
		gpioSpinners[6] = (Spinner) root.findViewById(R.id.new_gpio_6);
		gpioSpinners[7] = (Spinner) root.findViewById(R.id.new_gpio_7);

		final String[][] gpioArrs = new String[8][];

		final ArrayAdapter<String>[] gpioAdapters = new ArrayAdapter[8];
		for(int i = 0; i < 8; i++)
		{
			gpioArrs[i] = getResources().getStringArray(R.array.gpio_pin_array);
			gpioAdapters[i] = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, gpioArrs[i]);
					//ArrayAdapter.createFromResource(this.getActivity(), R.array.gpio_pin_array, android.R.layout.simple_spinner_item);
			gpioAdapters[i].setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
			gpioSpinners[i].setAdapter(gpioAdapters[i]);
		}

		final Spinner sensorSpinner = (Spinner) root.findViewById(R.id.sensor_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.sensors_array, R.layout.support_simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		sensorSpinner.setAdapter(adapter);
		sensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				for(int i = 0; i < 8; i++)
					gpioSpinners[i].setBackgroundColor(getResources().getColor(R.color.clear));

				switch((String) parent.getItemAtPosition(position))
				{
					case "KeyPad":

						for(int i = 0; i < 8; i++)
						{
							gpioSpinners[i].setVisibility(View.VISIBLE);
							gpioArrs[i][0] = ((i > 3) ? "C" : "R") + (i%4 + 1);
							gpioAdapters[i].notifyDataSetChanged();
						}

						break;

					case "Button":
						gpioSpinners[0].setVisibility(View.VISIBLE);
						gpioArrs[0][0] = "IN";
						gpioAdapters[0].notifyDataSetChanged();

						for(int i = 1; i < 8; i++)
							gpioSpinners[i].setVisibility(View.INVISIBLE);

					case "Pulse Oximeter":
						gpioSpinners[0].setVisibility(View.VISIBLE);
						gpioArrs[0][0] = "SCL";
						gpioAdapters[0].notifyDataSetChanged();

						gpioSpinners[1].setVisibility(View.VISIBLE);
						gpioArrs[1][0] = "SDA";
						gpioAdapters[1].notifyDataSetChanged();

						for(int i = 2; i < 8; i++)
							gpioSpinners[i].setVisibility(View.INVISIBLE);
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
				boolean sensorAdded = true;

				for(int i = 0; i < 8; i++)
					gpioSpinners[i].setBackgroundColor(getResources().getColor(R.color.clear));

				switch ((String) sensorSpinner.getSelectedItem())
				{
					case "KeyPad":

						for (int i = 0; i < 8; i++)
						{
							if (gpioSpinners[i].getSelectedItemPosition() == 0)
							{
								gpioSpinners[i].setBackgroundColor(getResources().getColor(R.color.badSelection));
								sensorAdded = false;
							}
						}
						if(sensorAdded)
							sensors.add(new KeyPad(sensorName.getText().toString(),
									Integer.parseInt((String) gpioSpinners[0].getSelectedItem()),
									Integer.parseInt((String) gpioSpinners[1].getSelectedItem()),
									Integer.parseInt((String) gpioSpinners[2].getSelectedItem()),
									Integer.parseInt((String) gpioSpinners[3].getSelectedItem()),
									Integer.parseInt((String) gpioSpinners[4].getSelectedItem()),
									Integer.parseInt((String) gpioSpinners[5].getSelectedItem()),
									Integer.parseInt((String) gpioSpinners[6].getSelectedItem()),
									Integer.parseInt((String) gpioSpinners[7].getSelectedItem())));
						break;

					case "Button":
						if (gpioSpinners[0].getSelectedItemPosition() > 0)
							sensors.add(new ButtonSensor(sensorName.getText().toString(),
									Integer.parseInt((String) gpioSpinners[0].getSelectedItem())));
						else
						{
							sensorAdded = false;
							gpioSpinners[0].setBackgroundColor(getResources().getColor(R.color.badSelection));
						}
						break;

					case "Pulse Oximeter":
						for (int i = 0; i < 2; i++)
						{
							if (gpioSpinners[i].getSelectedItemPosition() == 0)
							{
								gpioSpinners[i].setBackgroundColor(getResources().getColor(R.color.badSelection));
								sensorAdded = false;
							}
						}
						if(sensorAdded)
							sensors.add(new PulseOximeter(sensorName.getText().toString(),
									Integer.parseInt((String) gpioSpinners[0].getSelectedItem()),
									Integer.parseInt((String) gpioSpinners[1].getSelectedItem())));

						break;
					default:
						sensorAdded = false;
						break;
				}

				if (sensorAdded)
				{
					sensorAdapter.notifyItemInserted(sensors.size() - 1);
					newSensorCard.setVisibility(View.INVISIBLE);
				}
			}
		});

		return root;
	}

	public void updateCurrentMode(Mode mode)
	{
		this.currentMode = mode;
	}
}
