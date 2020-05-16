package com.smes.smes_android.ui.mode;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
	private SecureData modesFile;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState)
	{
		modeViewModel =
				ViewModelProviders.of(this).get(com.smes.smes_android.ui.mode.ModeViewModel.class);

		//Inflate the view of the Mode fragment to interact with view elements
		final View root = inflater.inflate(R.layout.fragment_mode, container, false);

		//Open up the current mode file if available and set the current mode
		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
			this.currentMode = (Mode) currentModeFile.readObjectData();
		}

		//If no current mode file is available then create a root mode
		catch (NullPointerException | IOException | ClassNotFoundException e)
		{
			currentModeFile = null;
			currentMode = new Mode("root", "Full Access", true, true, true, false);
		}

		//Open up the mode list SecureData file if it is available and add listed modes to local
		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			modesFile = new SecureData("Mode List", "all_modes.txt", externalDir);
			this.modes = (ArrayList<Mode>) modesFile.readObjectData();
			if(this.modes.size() == 0)
				this.modes.add(this.currentMode);

		}

		//If no list is available then create a new one and add the current mode to it
		catch (IOException e)
		{
			modes = new ArrayList<>();
			modes.add(currentMode);
		} catch (ClassNotFoundException e) {}


		//Write the current mode to the current mode file in case it has not yet been entered
		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			if(currentModeFile == null)
				currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
			currentModeFile.writeData(this.currentMode);
		}
		catch (NullPointerException | IOException e){}


		//The view for the list of Modes and its adapter
		RecyclerView modeList = (RecyclerView) root.findViewById(R.id.mode_list);
		modeList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		final ModeAdapter modeAdapter = new ModeAdapter(modes);
		this.currentMode = modeAdapter.getModeFromName(this.currentMode.toString());
		if(this.currentMode == null)
			this.currentMode = modeAdapter.getModeAt(0);

		//If a mode is selected from the list then make it the active mode and save it to the current mode file
		modeAdapter.setClickListener(new ModeAdapter.ItemClickListener()
		{
			@Override
			public void onItemClick(View view, int position)
			{
				modeAdapter.selectModeAt(position);
				Log.i("Click", "" + position);
				currentMode = modeAdapter.getCurrentMode();

				try
				{
					File externalDir = getContext().getExternalFilesDir(null);
					if(currentModeFile == null)
						currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
					currentModeFile.writeData(currentMode);
				}
				catch (NullPointerException | IOException e){}
			}
		});
		modeList.setAdapter(modeAdapter);

		//Start with selected mode in adapter as the initially active mode
		modeAdapter.setCurrentMode(this.currentMode);

		//Open new mode creation dialogue when pressing the plus button
		FloatingActionButton fb = (FloatingActionButton) root.findViewById(R.id.new_mode_button);
		final CardView newModeCard = (CardView) root.findViewById(R.id.new_mode_card);
		fb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currentMode.creatingModesOK())
					newModeCard.setVisibility(View.VISIBLE);

				//If the current mode cannot create new modes then block the card and notify user
				else
				{
					Snackbar.make(root, "Mode " + currentMode.toString() + " does not have mode creation privileges", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
				}

			}
		});

		//New mode creation dialogue items
		Button button = (Button) root.findViewById(R.id.create_mode_button);
		final EditText nameText = ((EditText) root.findViewById(R.id.new_mode_name));
		final EditText modeSubtext = ((EditText) root.findViewById(R.id.new_mode_desc));
		final Switch modeCrSwitch = (Switch) root.findViewById(R.id.is_mode_create);
		final Switch sensCrSwitch = (Switch) root.findViewById(R.id.is_sensor_create);
		final Switch dataCrSwitch = (Switch) root.findViewById(R.id.is_data_create);
		final Switch roCrSwitch = (Switch) root.findViewById(R.id.is_read_only);

		//When clicked create a new mode based on the settings in the dialogue
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String newModeName = nameText.getText().toString();
				String newModeSubtext = modeSubtext.getText().toString();

				Mode newMode = new Mode(newModeName, newModeSubtext);
				modes.add(newMode);
				modeAdapter.notifyItemInserted(modes.size() - 1);
				newModeCard.setVisibility(View.INVISIBLE);

				try
				{
					File externalDir = getContext().getExternalFilesDir(null);
					if(modesFile == null)
						modesFile = new SecureData("Mode List", "all_modes.txt", externalDir);
					modesFile.writeData(modes);
				} catch (IOException e){}
			}
		});


		return root;
	}
}
