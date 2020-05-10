package com.smes.smes_android.ui.mode;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ModeViewModel extends ViewModel
{

	private MutableLiveData<String> mText;

	public ModeViewModel()
	{
		mText = new MutableLiveData<>();
		mText.setValue("This is notifications fragment");
	}

	public LiveData<String> getText()
	{
		return mText;
	}
}