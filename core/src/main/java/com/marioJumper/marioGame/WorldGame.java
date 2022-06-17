package com.marioJumper.marioGame;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.marioJumper.SongGame;
import com.marioJumper.objects.Counter;
import com.marioJumper.objects.Mario;
import com.marioJumper.objects.Pipe;
import com.marioJumper.screens.Screens;

public class WorldGame {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    static final int STATE_RUNNING = 0;
    static final int STATE_GAMEOVER = 1;
    public int state;

    static final float TIME_PIPE = 1.5f;
    float timePipe;

    public World oWorldBox;
    public int score;

    Mario mario;

    Array<Body> arrBody;
    Array<Pipe> arrPipe;

    public WorldGame(){
        oWorldBox = new World(new Vector2(0, -13.0f), true);
        oWorldBox.setContactListener(new Colisions());
        arrBody = new Array<>();
        arrPipe = new Array<>();
        timePipe = 1.5f;
        createMario();
        createRoof();
        createFloor();
        state = STATE_RUNNING;
    }

    private void createMario() {
        mario = new Mario(1.35f, 4.75f);

        BodyDef bd = new BodyDef();
        bd.position.x = mario.position.x;
        bd.position.y = mario.position.y;
        bd.type = BodyDef.BodyType.DynamicBody;

        Body oBody =  oWorldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(.25f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 8;
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setUserData(mario);
        oBody.setBullet(true);
        shape.dispose();
    }

    private void createRoof() {

        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = HEIGHT;
        bd.type = BodyDef.BodyType.StaticBody;

        Body oBody =  oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, WIDTH, 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        oBody.createFixture(fixture);

        shape.dispose();
    }

    private void createFloor() {

        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = 1.1f;
        bd.type = BodyDef.BodyType.StaticBody;

        Body oBody =  oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, WIDTH, 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        oBody.createFixture(fixture);

        shape.dispose();
    }

    private void addPipe(){
        float x = WIDTH + 2.5f;
        float y = MathUtils.random() * 1.5f + .4f;

        addPipe(x, y, false);
        addPipe(x, y + 2f + Pipe.HEIGHT, true);

        addCounter(x, y + Counter.HEIGHT /2f + Pipe.HEIGHT/2f + .1f);
    }

    private void addPipe (float x, float y, boolean isTopPipe){
        Pipe obj;
        if(isTopPipe)
            obj = new Pipe(x, y, Pipe.TYPE_UP);
        else
            obj = new Pipe(x, y, Pipe.TYPE_DOWN);

        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyDef.BodyType.KinematicBody;

        Body oBody =  oWorldBox.createBody(bd);
        oBody.setLinearVelocity(Pipe.SPEED_X, 0);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Pipe.WIDTH/2f, Pipe.HEIGHT/2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;

        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setUserData(obj);
        arrPipe.add(obj);
        shape.dispose();
    }

    private void addCounter (float x, float y){
        Counter obj = new Counter();

        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyDef.BodyType.KinematicBody;

        Body oBody =  oWorldBox.createBody(bd);
        oBody.setLinearVelocity(Counter.SPEED_X, 0);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Counter.WIDTH/2f, Counter.HEIGHT/2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;

        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setUserData(obj);
        shape.dispose();
    }

    public void update(float delta, boolean jump){
        oWorldBox.step(delta, 8, 4);
        deleteObject();
        oWorldBox.getBodies(arrBody);

        timePipe += delta;
        if(timePipe >= TIME_PIPE){
            timePipe -= TIME_PIPE;
            addPipe();
        }

        for(Body body : arrBody){
            if(body.getUserData() instanceof Mario){
                updateMario(body, delta, jump);
            }else if(body.getUserData() instanceof Pipe){
                updatePipe(body);
            }else if(body.getUserData() instanceof Counter){
                updateCounter(body);
            }
        }

        if(mario.state == Mario.STATE_DEAD){
            state = STATE_GAMEOVER;
            SongGame.stopMusicabg();
            SongGame.playGameOver();
        }
    }

    private void deleteObject() {
        oWorldBox.getBodies(arrBody);
        for(Body body : arrBody){
            if (!oWorldBox.isLocked()){
                if(body.getUserData() instanceof Pipe){
                    Pipe obj = (Pipe) body.getUserData();
                    if(obj.state == Pipe.STATE_REMOVE){
                        arrPipe.removeValue(obj, true);
                        oWorldBox.destroyBody(body);
                    }
                }else if(body.getUserData() instanceof Counter){
                    Counter obj = (Counter) body.getUserData();
                    if(obj.state == Counter.STATE_REMOVE){
                        oWorldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    private void updateCounter(Body body) {
        if(mario.state == Mario.STATE_NORMAL){
            Counter obj = (Counter) body.getUserData();
            obj.update(body);
            if(obj.position.x <= -5){
                obj.state = Counter.STATE_REMOVE;
            }
        }else{
            body.setLinearVelocity(0, 0);
        }
    }

    private void updatePipe(Body body) {
        if(mario.state == Mario.STATE_NORMAL){
            Pipe obj = (Pipe) body.getUserData();

            obj.update(body);
            if(obj.position.x <= -5){
                obj.state = Pipe.STATE_REMOVE;
            }
        }else{
            body.setLinearVelocity(0, 0);
        }
    }

    private void updateMario(Body body, float delta, boolean jump){
        mario.update(delta, body);
        if(jump && mario.state == Mario.STATE_NORMAL){
            body.setLinearVelocity(0, Mario.JUMP_SPEED);
        }
    }

    class Colisions implements ContactListener{

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if(a.getBody().getUserData() instanceof Mario){
                beginContactMario(a, b);
            }else if(b.getBody().getUserData() instanceof Mario){
                beginContactMario(b, a);
            }

        }

        private void beginContactMario (Fixture mMario, Fixture fixElse){
            Object sElse = fixElse.getBody().getUserData();

            if(sElse instanceof Counter){

                Counter obj = (Counter) sElse;
                if(obj.state == Counter.STATE_NORMAL){
                    obj.state = Counter.STATE_REMOVE;
                    score++;
                    SongGame.playWin();
                }

            }else{
                if(mario.state == Mario.STATE_NORMAL){
                    mario.hurt();
                }
            }
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    }
}
