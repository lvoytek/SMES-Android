package com.smes.smes_android.ui.sensors;

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

import com.smes.smes_android.R;

public class SensorsFragment extends Fragment
{

	private SensorsViewModel sensorsViewModel;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		sensorsViewModel = ViewModelProviders.of(this).get(SensorsViewModel.class);
		View root = inflater.inflate(R.layout.fragment_sensors, container, false);
		final TextView textView = root.findViewById(R.id.text_sensors);
		sensorsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
		{
			@Override
			public void onChanged(@Nullable String s)
			{
				textView.setText(s);
			}
		});
		return root;
	}
}
