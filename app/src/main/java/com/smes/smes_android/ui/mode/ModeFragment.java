package com.smes.smes_android.ui.mode;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		currentMode = new Mode("root", true, true, true, false);
		modes.add(currentMode);

		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
			currentModeFile.writeData(this.currentMode);
		}
		catch (NullPointerException | IOException e){}

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
