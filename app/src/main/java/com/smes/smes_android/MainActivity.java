package com.smes.smes_android;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.smes_mode.Mode;
import com.smes.smes_android.ui.dashboard.DashboardFragment;
import com.smes.smes_android.ui.mode.ModeFragment;
import com.smes.smes_android.ui.sensors.SensorsFragment;


public class MainActivity extends AppCompatActivity
{
	SensorsFragment sensorsFrag;
	DashboardFragment dashboardFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		BottomNavigationView navView = findViewById(R.id.nav_view);
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
				R.id.navigation_dashboard, R.id.navigation_sensors, R.id.navigation_data, R.id.navigation_mode)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(navView, navController);
	}
	
}
