package com.smes.smes_android.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel
{

	private MutableLiveData<String> mText;

	public DashboardViewModel()
	{
		mText = new MutableLiveData<>();
		mText.setValue("You have not tested for root yet");
	}

	public LiveData<String> getText()
	{
		return mText;
	}
}