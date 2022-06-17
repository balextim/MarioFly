package com.marioJumper.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Mario {
    public static float JUMP_SPEED = 5;

    public static  int STATE_NORMAL = 0;
    public static int STATE_DEAD = 1;

    public int state;

    public Vector2 position;
    public float stateTime;

    public Mario(float x, float y){
        position = new Vector2(x, y);
        state = STATE_NORMAL;
    }

    public void update (float delta, Body body){
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        stateTime+= delta;
    }

    public void hurt(){
        state = STATE_DEAD;
        stateTime = 0;
    }

}
