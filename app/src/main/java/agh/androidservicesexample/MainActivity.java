package agh.androidservicesexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.*;

public class MainActivity extends AppCompatActivity
{
	private Button toggleButton;
	private boolean started = false;
	private Intent playerIntent;

	private static File getTempFile(Context context, String filename) {
		File file = null;
		try {
			file = File.createTempFile(filename, null, context.getCacheDir());
		} catch (IOException e) {

		}
		return file;
	}

	private FileOutputStream getTempFileOutputStream(){
		File file = new File(getCacheDir(), ".state");
		try {
			return new FileOutputStream(file);
		}
		catch (FileNotFoundException e) {
			return null;
		}
	}

	private FileInputStream getTempFileInputStream(){
		File file = getTempFile(this, ".state");
		if (file != null) {
			try {
				return new FileInputStream(file);
			}
			catch (FileNotFoundException e) {
				return null;
			}
		}
		return null;
	}

	private void persistState(){
		FileOutputStream s = getTempFileOutputStream();
		if (s != null){
			try {
				s.write(started ? 1 : 0);
				s.close();
			}
			catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
			}
		}
	}

	private void loadState(){
		FileInputStream s = getTempFileInputStream();
		if (s != null){
			try {
				int val = s.read();
				s.close();
				started = val == 1;
				toggleButton.setText(started ? "PAUSE" : "PLAY");
			}
			catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toggleButton =  findViewById(R.id.toggleButton);
		playerIntent = new Intent(this, PlayerService.class);
		loadState();
	}


	@Override
	protected void onDestroy() {
		persistState();
		super.onDestroy();
	}


	public void onToggleButtonClicked(View view) {
		if (started){
			stopService(playerIntent);
			toggleButton.setText("PLAY");
			started=false;
		}
		else{
			startService(playerIntent);
			toggleButton.setText("PAUSE");
			started=true;
		}
	}
}
