package com.smes.smes_android.ui.mode;

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

public class ModeFragment extends Fragment
{

	private com.smes.smes_android.ui.mode.ModeViewModel modeViewModel;

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
		return root;
	}
}
