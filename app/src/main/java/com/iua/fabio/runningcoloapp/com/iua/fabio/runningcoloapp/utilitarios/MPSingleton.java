package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class MPSingleton {
    private static MPSingleton _instance=null;
    private static MediaPlayer _mp=null;
    private int firstSong=0;
    private int currentPos=0;

    public MPSingleton() {
        _mp=new MediaPlayer();
    }

    public static MPSingleton getInstance(){
        if(_instance==null){
            _instance=new MPSingleton();
        }

        return _instance;
    }

    public void prepareMedia(Context mContext, int res){
        if(firstSong==0 || firstSong!=res) {
            getInstance().get_mp().reset();
            firstSong=res;
            AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(res);

            try {
                getInstance().get_mp().setDataSource(afd);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                getInstance().get_mp().prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentPos=getInstance().get_mp().getCurrentPosition();
        if(firstSong==res && getInstance().get_mp().isPlaying()){
            getInstance().get_mp().seekTo(currentPos);
        }
    }

    public MediaPlayer get_mp() {
        return getInstance()._mp;
    }

    public void play(){
        if(!getInstance().get_mp().isPlaying()){
            getInstance().get_mp().start();
        }
    }

    public void pauseSong(){
        getInstance().get_mp().pause();
    }

    public void stopSong(){
        if(getInstance().get_mp().isPlaying()){
            getInstance().get_mp().stop();

            try {
                getInstance().get_mp().prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
