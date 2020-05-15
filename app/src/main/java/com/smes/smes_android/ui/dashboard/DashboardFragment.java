package com.smes.smes_android.ui.dashboard;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class DashboardFragment extends Fragment
{

	private DashboardViewModel dashboardViewModel;

	private Mode currentMode;
	private SecureData currentModeFile;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState)
	{
		dashboardViewModel =
				ViewModelProviders.of(this).get(DashboardViewModel.class);
		View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
		final TextView textView = root.findViewById(R.id.text_dashboard);
		dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
		{
			@Override
			public void onChanged(@Nullable String s)
			{
				textView.setText(s);
			}
		});


		Button button = (Button) root.findViewById(R.id.test_button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currentMode != null)
					textView.setText(currentMode.toString());
			}
		});

		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
			this.currentMode = (Mode) currentModeFile.readObjectData();
		}
		catch (NullPointerException | IOException | ClassNotFoundException e){}

		getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged()
			{
				try
				{
					File externalDir = getContext().getExternalFilesDir(null);
					currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
					currentMode = (Mode) currentModeFile.readObjectData();
				}
				catch (NullPointerException | IOException | ClassNotFoundException e){}
			}
		});

		return root;
	}

	public void updateCurrentMode(Mode mode)
	{
		this.currentMode = mode;
	}
}
