package com.marioJumper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.marioJumper.marioGame.GameScreen;

public class MainMarioJumper extends Game {
	@Override
	public void create() {
		Assets.load();
		setScreen(new GameScreen(this));
	}
}