package it.uniba.dib.sms2223_28;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

public class BackgroundMusic {

    static private boolean musicFlag;
    static private MediaPlayer mediaPlayer;
    public static void start(@NonNull Context context, @NonNull Integer musicId) {
        if (mediaPlayer==null) {
            mediaPlayer = MediaPlayer.create(context, musicId);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public static void stop(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
            mediaPlayer=null;
        }
    }

    public static boolean isMusicFlag() {
        return musicFlag;
    }

    public static void setMusicFlag(boolean musicFlag) {
        BackgroundMusic.musicFlag = musicFlag;
    }


}
