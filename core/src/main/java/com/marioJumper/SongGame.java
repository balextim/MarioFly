package com.marioJumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SongGame {
    static Music musicabg = Gdx.audio.newMusic(Gdx.files.internal("data/musica.mp3"));
    static Music win = Gdx.audio.newMusic(Gdx.files.internal("data/win.mp3"));
    static Music gameover = Gdx.audio.newMusic(Gdx.files.internal("data/lose.mp3"));

    public static void playMusicabg(){
        musicabg.play();
        musicabg.setLooping(true);
    }

    public static void stopMusicabg(){
        musicabg.stop();
    }

    public static void playWin(){
        win.play();
    }

    public static void stopWin(){
        win.stop();
    }

    public static void playGameOver(){
        gameover.play();
    }

    public static void stopGameOver(){
        gameover.stop();
    }
}
