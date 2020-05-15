package com.smes.smes_android.ui.data;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class DataFragment extends Fragment
{

	private com.smes.smes_android.ui.data.DataViewModel dataViewModel;
	private ArrayList<SecureData> datas;
	private SecureData dataListFile;
	private SecureData currentModeFile;
	private Mode currentMode;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState)
	{
		dataViewModel =
				ViewModelProviders.of(this).get(com.smes.smes_android.ui.data.DataViewModel.class);
		final View root = inflater.inflate(R.layout.fragment_data, container, false);

		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			dataListFile = new SecureData("Data List", "all_datas.txt", externalDir);
			this.datas = (ArrayList<SecureData>) dataListFile.readObjectData();

		} catch (IOException e)
		{
			datas = new ArrayList<>();
		} catch (ClassNotFoundException e) {}


		RecyclerView dataList = (RecyclerView) root.findViewById(R.id.data_list);
		dataList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		final DataAdapter dataAdapter = new DataAdapter(datas);
		dataList.setAdapter(dataAdapter);

		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
			this.currentMode = (Mode) currentModeFile.readObjectData();
		}
		catch (NullPointerException | IOException | ClassNotFoundException e){}


		FloatingActionButton floatButton = (FloatingActionButton) root.findViewById(R.id.new_data_button);
		final CardView newDataCard = ((CardView) root.findViewById(R.id.new_data_card));
		floatButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currentMode == null || currentMode.creatingDataOK())
					newDataCard.setVisibility(View.VISIBLE);
				else
				{
					Snackbar.make(root, "Mode " + currentMode.toString() + " does not have data creation privileges", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
				}
			}
		});

		final EditText dataName = (EditText) root.findViewById(R.id.new_data_name);
		final EditText dataFileName = (EditText) root.findViewById(R.id.new_data_filename);
		final EditText dataData = (EditText) root.findViewById(R.id.data_input);
		final String reservedFileChars = "|\\?*<\":>+[]/'";
		Button button = (Button) root.findViewById(R.id.save_button);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean newDataSuccess = true;

				dataName.setBackgroundColor(getResources().getColor(R.color.clear));
				dataFileName.setBackgroundColor(getResources().getColor(R.color.clear));

				String nameStr = dataName.getText().toString();
				String fileNameStr = dataFileName.getText().toString();
				String dataStr = dataData.getText().toString();

				if(nameStr.length() == 0)
				{
					dataName.setBackgroundColor(getResources().getColor(R.color.badSelection));
					newDataSuccess = false;
				}

				if(fileNameStr.length() == 0)
				{
					dataFileName.setBackgroundColor(getResources().getColor(R.color.badSelection));
					newDataSuccess = false;
				}

				if(newDataSuccess)
				{
					for(int i = 0; i < reservedFileChars.length(); i++)
					{
						if(fileNameStr.indexOf(reservedFileChars.charAt(i)) > -1)
						{
							dataFileName.setBackgroundColor(getResources().getColor(R.color.badSelection));
							newDataSuccess = false;
							break;
						}
					}
				}

				if(newDataSuccess)
				{
					try
					{
						File externalDir = getContext().getExternalFilesDir(null);
						datas.add(new SecureData(nameStr, fileNameStr, externalDir));
						datas.get(datas.size() - 1).writeData(dataStr);
					} catch (IOException e)
					{
						newDataSuccess = false;
						dataFileName.setBackgroundColor(getResources().getColor(R.color.badSelection));
					}

					if(newDataSuccess)
					{
						newDataCard.setVisibility(View.INVISIBLE);
						dataAdapter.notifyDataSetChanged();

						try
						{
							File externalDir = getContext().getExternalFilesDir(null);
							if(dataListFile == null)
								dataListFile = new SecureData("Data List", "all_datas.txt", externalDir);
							dataListFile.writeData(datas);
						} catch (IOException e){}
					}
				}
			}
		});


		return root;
	}
}
