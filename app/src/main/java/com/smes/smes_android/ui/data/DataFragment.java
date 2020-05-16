package com.smes.smes_android.ui.data;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
	private SecureData selectedSD;
	private Mode currentMode;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState)
	{
		dataViewModel =
				ViewModelProviders.of(this).get(com.smes.smes_android.ui.data.DataViewModel.class);

		//Inflate Secure Data view to interact with visual elements
		final View root = inflater.inflate(R.layout.fragment_data, container, false);

		//The pop-up cards for entering new data and viewing existing data
		final CardView newDataCard = ((CardView) root.findViewById(R.id.new_data_card));
		final CardView editDataCard = ((CardView) root.findViewById(R.id.edit_data_card));

		//Characters that are blocked in file name creation
		final String reservedFileChars = "|\\?*<\":>+[]/'";

		//Attempt to read the existing SecureData files from the filesystem if there is any
		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			dataListFile = new SecureData("Data List", "all_data.txt", externalDir);
			this.datas = (ArrayList<SecureData>) dataListFile.readObjectData();

		}
		//The SecureData file list has not yet been created so create a new one
		catch (IOException e)
		{
			dataListFile = null;
			datas = new ArrayList<>();
		} catch (ClassNotFoundException e) {}


		//Text components in the SecureData element and the update button
		final EditText editDataName = (EditText) root.findViewById(R.id.edit_data_name);
		final EditText editDataFileName = (EditText) root.findViewById(R.id.edit_data_filename);
		final EditText editDataData = (EditText) root.findViewById(R.id.edit_data_input);
		final Button updButton = (Button) root.findViewById(R.id.update_button);

		//The top left list of SecureData files and the adapter for the list to be viewed
		RecyclerView dataList = (RecyclerView) root.findViewById(R.id.data_list);
		dataList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		final DataAdapter dataAdapter = new DataAdapter(datas);

		//When a data item is clicked, select it and open it in the editor
		dataAdapter.setClickListener(new DataAdapter.ItemClickListener()
		{
			@Override
			public void onItemClick(View view, int position)
			{
				dataAdapter.selectDataAt(position);
				selectedSD = dataAdapter.getSelectedSD();

				if(selectedSD != null)
				{
					newDataCard.setVisibility(View.INVISIBLE);
					editDataCard.setVisibility(View.VISIBLE);

					editDataName.setText(selectedSD.toString());
					editDataFileName.setText(selectedSD.getFileName());

					try
					{
						editDataData.setText(selectedSD.readStringData());
					}
					catch(IOException e){}

					//Disable editing when the mode is read only
					if(currentMode != null && currentMode.isReadOnly())
					{
						editDataName.setEnabled(false);
						editDataFileName.setEnabled(false);
						editDataData.setEnabled(false);
						updButton.setVisibility(View.INVISIBLE);

					}
					else
					{
						editDataName.setEnabled(true);
						editDataFileName.setEnabled(true);
						editDataData.setEnabled(true);
						updButton.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		dataList.setAdapter(dataAdapter);

		//Button to write edited SecureData to file, make sure constraints are followed before writing
		updButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean editDataSuccess = true;

				if(selectedSD != null)
				{
					editDataName.setBackgroundColor(getResources().getColor(R.color.clear));
					editDataFileName.setBackgroundColor(getResources().getColor(R.color.clear));

					String nameStr = editDataName.getText().toString();
					String fileNameStr = editDataFileName.getText().toString();
					String dataStr = editDataData.getText().toString();

					//Name must not be empty, if it is turn its background red
					if(nameStr.length() == 0)
					{
						editDataName.setBackgroundColor(getResources().getColor(R.color.badSelection));
						editDataSuccess = false;
					}

					//Filename must not be empty
					if(fileNameStr.length() == 0)
					{
						editDataFileName.setBackgroundColor(getResources().getColor(R.color.badSelection));
						editDataSuccess = false;
					}

					//Confirm there are no reserved characters in the filename
					if(editDataSuccess)
					{
						for(int i = 0; i < reservedFileChars.length(); i++)
						{
							if(fileNameStr.indexOf(reservedFileChars.charAt(i)) > -1)
							{
								editDataFileName.setBackgroundColor(getResources().getColor(R.color.badSelection));
								editDataSuccess = false;
								break;
							}
						}
					}

					//If all constraints are followed then attempt to write the data to the file
					if(editDataSuccess)
					{
						try
						{
							selectedSD.writeStringData(dataStr);
						} catch (IOException e)
						{
							editDataSuccess = false;
							editDataFileName.setBackgroundColor(getResources().getColor(R.color.badSelection));
						}

						//If write succeeds then modify the SecureData item in the list and write to list file
						if(editDataSuccess)
						{
							dataAdapter.notifyDataSetChanged();

							try
							{
								File externalDir = getContext().getExternalFilesDir(null);
								if(dataListFile == null)
									dataListFile = new SecureData("Data List", "all_data.txt", externalDir);
								dataListFile.writeData(datas);
							} catch (IOException e){}
						}
					}
				}
			}
		});

		//Upon entering this fragment, attempt to get the current mode from the global mode file
		try
		{
			File externalDir = getContext().getExternalFilesDir(null);
			currentModeFile = new SecureData("Current Mode", "cmode.txt", externalDir);
			this.currentMode = (Mode) currentModeFile.readObjectData();
		}
		catch (NullPointerException | IOException | ClassNotFoundException e){}


		//The text elements for entering a new SecureData object
		final EditText dataName = (EditText) root.findViewById(R.id.new_data_name);
		final EditText dataFileName = (EditText) root.findViewById(R.id.new_data_filename);
		final EditText dataData = (EditText) root.findViewById(R.id.data_input);

		//Open the new data dialogue when the bottom right plus button is pressed
		FloatingActionButton floatButton = (FloatingActionButton) root.findViewById(R.id.new_data_button);
		floatButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(currentMode == null || currentMode.creatingDataOK())
				{
					//Clean up the new data dialogue card
					editDataCard.setVisibility(View.INVISIBLE);
					newDataCard.setVisibility(View.VISIBLE);
					dataName.setText("Data Name");
					dataFileName.setText("File Name");
					dataData.setText("");
				}

				//If mode does not have data create access then block it from creating and notify user
				else
				{
					Snackbar.make(root, "Mode " + currentMode.toString() + " does not have data creation privileges", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
				}
			}
		});


		//Creates a new data entry when clicked as long as input is valid
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
						datas.get(datas.size() - 1).writeStringData(dataStr);
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
								dataListFile = new SecureData("Data List", "all_data.txt", externalDir);
							dataListFile.writeData(datas);
						} catch (IOException e){}
					}
				}
			}
		});


		return root;
	}
}
