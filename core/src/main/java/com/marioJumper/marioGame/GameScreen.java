package com.marioJumper.marioGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.marioJumper.Assets;
import com.marioJumper.MainMarioJumper;
import com.marioJumper.SongGame;
import com.marioJumper.screens.Screens;

public class GameScreen extends Screens {

    static final int STATE_READY = 0;
    static final int STATE_RUNNING = 1;
    static final int STATE_GAMEOVER = 2;
    int state;

    WorldGame oWorld;
    WorldGameRenderer  renderer;

    private Music musicbg;

    Image getReady, tap, gameOver;

    public GameScreen(MainMarioJumper game){
        super(game);

        state = STATE_READY;

        oWorld = new WorldGame();
        renderer = new WorldGameRenderer(spriteBatch, oWorld);

        getReady = new Image(Assets.supermario);
        getReady.setHeight(180);
        getReady.setWidth(360);
        getReady.setPosition(SCREEN_WIDTH/2 - getReady.getWidth()/2, 550);

        tap = new Image(Assets.tap);
        tap.setHeight(70);
        tap.setWidth(70);
        tap.setPosition(SCREEN_WIDTH/2+5 - getReady.getWidth()/2, 400);

        gameOver = new Image(Assets.gameOver);
        gameOver.setPosition(SCREEN_WIDTH/2 - getReady.getWidth()/2, 500);

        stage.addActor(getReady);
        stage.addActor(tap);
    }

    @Override
    public void draw(float delta) {
        renderer.render(delta);

        oCam.update();
        spriteBatch.setProjectionMatrix(oCam.combined);

        spriteBatch.begin();
        String auxScore = oWorld.score + "";
        float width = Assets.getTextWidth(auxScore);
        Assets.font.draw(spriteBatch, auxScore, SCREEN_WIDTH/2f - width/2, 700);
        spriteBatch.end();
    }

    @Override
    public void update(float delta) {
        switch (state){
            case STATE_READY:
                updateReady(delta);
                break;
            case STATE_RUNNING:
                updateRunning(delta);
                break;
            case STATE_GAMEOVER:
                updateGameOver(delta);
                break;
        }
    }

    private void updateGameOver(float delta) {

        if(Gdx.input.justTouched()){
            gameOver.addAction(Actions.sequence(
                    Actions.fadeOut(.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            gameOver.remove();
                            SongGame.stopGameOver();
                            game.setScreen(new GameScreen(game));
                        }
                    })
            ));
        }

    }

    private void updateRunning(float delta) {

        boolean jump = Gdx.input.justTouched();
        if(jump){
            //Salto
        }
        oWorld.update(delta, jump);

        if(oWorld.state == WorldGame.STATE_GAMEOVER){
            state = STATE_GAMEOVER;
            stage.addActor(gameOver);
            SongGame.stopMusicabg();
            SongGame.playGameOver();
        }
    }

    private void updateReady(float delta) {
        if(Gdx.input.justTouched()){
            getReady.addAction(Actions.fadeOut(.3f));
            tap.addAction(Actions.sequence(
                    Actions.fadeOut(.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            getReady.remove();
                            tap.remove();
                            state = STATE_RUNNING;
                            SongGame.playMusicabg();
                        }
                    })
            ));
        }
    }
}
