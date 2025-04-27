package com.example.wordly_by_yuvalmiz;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener {

    MediaPlayer mediaPlayer;
    @Override
    public void onCompletion(MediaPlayer mp) {  //MediaPlayer
        Toast.makeText(this,"onCompletion",Toast.LENGTH_SHORT).show();

        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
        //stopSelf(); // to stop the Service

        Toast.makeText(PlayService.this, "I'm Finished", Toast.LENGTH_SHORT).show();
        mediaPlayer=MediaPlayer.create(this,R.raw.gamelobbymusic);

        mediaPlayer.setLooping(true); // restart play again
        mediaPlayer.start();

    }

    @Override
    public void onCreate() { // belong to Service
        super.onCreate();
        Toast.makeText(this,"Service-onCreate",Toast.LENGTH_SHORT).show();
        mediaPlayer=MediaPlayer.create(this,R.raw.gamelobbymusic);
        mediaPlayer.setLooping(false);
        // call onCompletion at the end of the song
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { // belong to Service
        //return super.onStartCommand(intent, flags, startId);
        Toast.makeText(this,"Service-OnStartCommend",Toast.LENGTH_SHORT).show();

        mediaPlayer.setLooping(false); // not restart play again
        mediaPlayer.setVolume(23,23);
        mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() { // belong to Service
        super.onDestroy();

        Toast.makeText(this,"Service-onDestroy",Toast.LENGTH_SHORT).show();
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
        mediaPlayer.release(); // free memory
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
