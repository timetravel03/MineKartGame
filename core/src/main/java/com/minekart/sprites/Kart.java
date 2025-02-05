package com.minekart.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.minekart.MineKart;
import com.minekart.screens.PlayScreen;

public class Kart extends Sprite {
    public World world;
    public Body b2Body;
    private TextureRegion marioStand;
    public final float X_KART_SPEED = 1.5f;
    private int puntuacion;

    public enum State {FALLING, JUMPING, STANDING, RUNNING}

    public State currentState;
    public State previousState;
    private float stateTimer;

    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private boolean runningRight;

    Sprite minecart;
    Sprite tabaco;
    Sprite minecart_back;

    Texture textura_tabaco;
    Texture textura_minecart;
    Texture textura_minecart_back;

    Vector2 posicionActual;

    public Kart(World world, PlayScreen screen) {
//        super(screen.getAtlas().findRegion("little_mario"));
//        super(screen.getAtlas().findRegion(""));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        textura_tabaco = new Texture("marlboro_64.png");
        textura_minecart = new Texture("minecart_64.png");
        textura_minecart_back = new Texture("minecart_64_back.png");

        minecart = new Sprite(textura_minecart);
        tabaco = new Sprite(textura_tabaco);
        minecart_back = new Sprite(textura_minecart_back);

        set(minecart);
        setBounds(0, 0, 24 / MineKart.PPM, 24 / MineKart.PPM);
        tabaco.setBounds(0, 0, getWidth(), getHeight());
        minecart_back.setBounds(0, 0, getWidth(), getHeight());

        runningRight = true;
        defineKart();

        posicionActual = new Vector2(b2Body.getPosition().x - minecart.getWidth() / 2, b2Body.getPosition().y - minecart.getHeight() / 2);
    }

    // propiedades fisicas yesyes
    public void defineKart() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(512 / MineKart.PPM, 256 / MineKart.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(9 / MineKart.PPM);
        fDef.filter.categoryBits = MineKart.KART_BIT;
        // bitwise operation (revisar) mascara (con lo que puede colisionar el personaje)
        fDef.filter.maskBits = MineKart.DEFAULT_BIT | MineKart.OBSTACLE_BIT;

        fDef.shape = circleShape;
        fDef.friction = 0.1f;
//        fDef.isSensor = true;
        b2Body.createFixture(fDef).setUserData(this);

        // colision cabeza (test)
//        hola

//        EdgeShape front = new EdgeShape();
//        front.set(new Vector2(7 / MineKart.PPM, 7 / MineKart.PPM), new Vector2(7 / MineKart.PPM, -7 / MineKart.PPM));
//        fDef.shape = front;
//        fDef.isSensor = true;
//        b2Body.createFixture(fDef).setUserData("front");


    }

    public State getState() {
        if (b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (b2Body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2Body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void sumaPuntuacion() {
        puntuacion += 100;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        posicionActual.lerp(new Vector2(x, y), .6f);
    }

    @Override
    public void draw(Batch batch) {
        minecart_back.draw(batch);
        tabaco.draw(batch);
        super.draw(batch);
    }

    public void update(float dt) {
        // mover el sistema de coordenadas desde el centro del círculo a la esquina inferior izq
        setPosition(b2Body.getPosition().x - getWidth() / 2, (b2Body.getPosition().y - getHeight() / 2));
//        setRegion(getFrame(dt));
        tabaco.setPosition(b2Body.getPosition().x - getWidth() / 2, posicionActual.y + 5 / MineKart.PPM);
        minecart_back.setPosition(b2Body.getPosition().x - getWidth() / 2, (b2Body.getPosition().y - getHeight() / 2) + 1 / MineKart.PPM);
//        tabaco.setPosition(b2Body.getPosition().x - tabaco.getWidth() / 2, b2Body.getPosition().y - tabaco.getHeight());
    }

    public void kartInput(float dt) {
//        && this.b2Body.getLinearVelocity().y == 0 debug
        if ((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.justTouched())) {
            this.b2Body.applyLinearImpulse(new Vector2(0, 4f), this.b2Body.getWorldCenter(), true);
//            this.b2Body.applyForce(new Vector2(0, 200f), this.b2Body.getWorldCenter(), true);
        }
//        if (this.b2Body.getLinearVelocity().x < X_KART_SPEED) {
//            this.b2Body.applyLinearImpulse(new Vector2(.2f, 0), this.b2Body.getWorldCenter(), true);
//        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && this.b2Body.getLinearVelocity().x <= 2) {
//            this.b2Body.applyLinearImpulse(new Vector2(.1f, 0), this.b2Body.getWorldCenter(), true);
            this.b2Body.applyForce(new Vector2(4f, 0), this.b2Body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && this.b2Body.getLinearVelocity().x >= -2) {
//            this.b2Body.applyLinearImpulse(new Vector2(-.1f, 0), this.b2Body.getWorldCenter(), true);
            this.b2Body.applyForce(new Vector2(-4f, 0), this.b2Body.getWorldCenter(), true);
        }
    }

}
