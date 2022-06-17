package com.marioJumper.marioGame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.marioJumper.Assets;
import com.marioJumper.objects.Mario;
import com.marioJumper.objects.Pipe;
import com.marioJumper.screens.Screens;

public class WorldGameRenderer {

    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    SpriteBatch spriteBatch;
    WorldGame oWorld;
    OrthographicCamera oCam;

    Box2DDebugRenderer renderBox;

    public WorldGameRenderer(SpriteBatch batch, WorldGame oWorld){
        this.oCam = new OrthographicCamera(WIDTH, HEIGHT);
        this.oCam.position.set(WIDTH/2f, HEIGHT/2, 0);
        this.spriteBatch = batch;
        this.oWorld = oWorld;
        renderBox = new Box2DDebugRenderer();
    }

    public void render (float delta){
        oCam.update();
        spriteBatch.setProjectionMatrix(oCam.combined);

        spriteBatch.begin();
        spriteBatch.disableBlending();
        drawBackground(delta);
        spriteBatch.enableBlending();
        drawPipes(delta);
        drawMario(delta);
        spriteBatch.end();

    }

    private void drawBackground(float delta) {
        spriteBatch.draw(Assets.background, 0, 0, WIDTH, HEIGHT);
    }

    private void drawPipes(float delta) {
        for (Pipe obj: oWorld.arrPipe){
            if(obj.type == Pipe.TYPE_DOWN){
                spriteBatch.draw(Assets.pipe, obj.position.x - .5f, obj.position.y - 2f, 1, 4);
            }else{
                spriteBatch.draw(Assets.pipeup, obj.position.x - .5f, obj.position.y - 2f, 1, 4);
            }
        }
    }

    private void drawMario(float delta) {
        Mario obj = oWorld.mario;

        TextureRegion keyFrame;
        if(obj.state == Mario.STATE_NORMAL){
            keyFrame = Assets.mario.getKeyFrame(obj.stateTime, true);

        }else{
            keyFrame = Assets.mario.getKeyFrame(obj.stateTime, false);
        }

        spriteBatch.draw(keyFrame, obj.position.x - .3f, obj.position.y-.25f, .6f, .5f);
    }
}
