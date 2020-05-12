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

import com.example.smes_mode.Mode;
import com.smes.smes_android.R;

import java.util.ArrayList;

public class ModeFragment extends Fragment
{

	private com.smes.smes_android.ui.mode.ModeViewModel modeViewModel;
	private Mode currentMode;
	private ArrayList<Mode> modes;

	CurrentModeSender modeSendCallback;

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

		this.sendCurrentMode();

		return root;
	}

	public interface CurrentModeSender
	{
		public void sendMode(Mode mode);
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			modeSendCallback = (CurrentModeSender) activity;
		}
		catch(ClassCastException e)
		{

		}
	}

	public void sendCurrentMode()
	{
		modeSendCallback.sendMode(this.currentMode);
	}

	@Override
	public void onDetach()
	{
		modeSendCallback = null;
		super.onDetach();
	}
}
