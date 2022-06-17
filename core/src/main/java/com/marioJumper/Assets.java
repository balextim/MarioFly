package com.marioJumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

    public static BitmapFont font;
    private static final GlyphLayout glyphLayaout = new GlyphLayout();

    public static Animation<TextureAtlas.AtlasRegion> mario;

    public static TextureRegion background;
    public static TextureRegion gameOver;
    public static TextureRegion supermario;
    public static TextureRegion tap;
    public static TextureRegion pipe;
    public static TextureRegion pipeup;

    public static void load(){
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/marioflyAtlas.txt"));

        background = atlas.findRegion("world");
        gameOver = atlas.findRegion("gameOver");
        supermario = atlas.findRegion("superario");
        tap = atlas.findRegion("tap");
        pipe = atlas.findRegion("pipe");
        pipeup = atlas.findRegion("pipeup");

        mario = new Animation<TextureAtlas.AtlasRegion>(.3f, atlas.findRegion("mario1"), atlas.findRegion("mario2"));

        font = new BitmapFont();
        font.getData().scale(5f);


    }

    public static float getTextWidth (String text){
        glyphLayaout.setText(font, text);
        return glyphLayaout.width;
    }
}
