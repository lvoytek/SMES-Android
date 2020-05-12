package com.smes.smes_android.ui.mode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.example.smes_data.SecureData;
import com.example.smes_mode.Mode;
import com.smes.smes_android.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ModeFragment extends Fragment
{

	private com.smes.smes_android.ui.mode.ModeViewModel modeViewModel;
	private Mode currentMode;
	private ArrayList<Mode> modes;
	private SecureData currentModeFile;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState)
	{
		modeViewModel =
				ViewModelProviders.of(this).get(com.smes.smes_android.ui.mode.ModeViewModel.class);
		View root = inflater.inflate(R.layout.fragment_mode, container, false);
		final TextView textView = root.findViewById(R.id.text_notifications);
		modeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
		{
			@Override
			public void onChanged(@Nullable String s)
			{
				textView.setText(s);
			}
		});


		modes = new ArrayList<>();
		currentMode = new Mode("root", "Full Access", true, true, true, false);
		modes.add(currentMode);

		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
			currentModeFile.writeData(this.currentMode);
		}
		catch (NullPointerException | IOException e){}


		RecyclerView modeList = (RecyclerView) root.findViewById(R.id.mode_list);
		modeList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		final ModeAdapter sensorAdapter = new ModeAdapter(modes);
		modeList.setAdapter(sensorAdapter);

		Button button = (Button) root.findViewById(R.id.new_mode_button);
		final CardView newModeCard = (CardView) root.findViewById(R.id.new_mode_card);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				newModeCard.setVisibility(View.VISIBLE);
			}
		});

		button = (Button) root.findViewById(R.id.create_mode_button);
		final EditText nameText = ((EditText) root.findViewById(R.id.new_mode_name));
		final EditText modeSubtext = ((EditText) root.findViewById(R.id.new_mode_desc));
		final Switch modeCrSwitch = (Switch) root.findViewById(R.id.is_mode_create);
		final Switch sensCrSwitch = (Switch) root.findViewById(R.id.is_sensor_create);
		final Switch dataCrSwitch = (Switch) root.findViewById(R.id.is_data_create);
		final Switch roCrSwitch = (Switch) root.findViewById(R.id.is_read_only);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String newModeName = nameText.getText().toString();
				String newModeSubtext = modeSubtext.getText().toString();

				Mode newMode = new Mode(newModeName, newModeSubtext);
				modes.add(newMode);
				sensorAdapter.notifyItemInserted(modes.size() - 1);
				newModeCard.setVisibility(View.INVISIBLE);
			}
		});


		return root;
	}


	public void saveCurrentMode()
	{
		try
		{
			this.currentModeFile.writeData(this.currentMode);
		}
		catch (IOException e){}
	}


}
