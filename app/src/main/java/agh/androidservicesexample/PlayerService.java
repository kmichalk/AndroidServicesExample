package agh.androidservicesexample;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

public class PlayerService extends Service
{
	private MediaPlayer player;

	@Override
	public IBinder onBind(Intent intent) {
		return  null;
	}

	@Override
	public void onCreate() {
		player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
		player.setLooping(true);
		Toast.makeText(this, "Player initialized", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		player.start();
		Toast.makeText(this, "Player started", Toast.LENGTH_LONG).show();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		player.stop();
		Toast.makeText(this, "Player stopped", Toast.LENGTH_LONG).show();
	}
}
