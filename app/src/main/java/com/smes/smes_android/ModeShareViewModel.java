package com.smes.smes_android;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.smes_mode.Mode;

public class ModeShareViewModel extends ViewModel
{
	private final MutableLiveData<Mode> selected = new MutableLiveData<Mode>();

	public void select(Mode mode) {
		selected.setValue(mode);
	}

	public LiveData<Mode> getSelected() {
		return selected;
	}
}

