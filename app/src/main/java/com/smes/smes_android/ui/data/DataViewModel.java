package com.smes.smes_android.ui.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DataViewModel extends ViewModel
{

	private MutableLiveData<String> mText;

	public DataViewModel()
	{
		mText = new MutableLiveData<>();
		mText.setValue("This is data fragment");
	}

	public LiveData<String> getText()
	{
		return mText;
	}
}