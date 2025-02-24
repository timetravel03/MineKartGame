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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.minekart.MineKart;
import com.minekart.screens.Fase;
import com.minekart.screens.PlayScreen;
import com.minekart.tools.CheckPoint;

public class Kart extends Sprite {
    public World world;
    public Body b2Body;
    private TextureRegion marioStand;
    public final float X_KART_SPEED = 1.5f;
    private int puntuacion;
    private int cantidad_monedas;
    private int cantidad_vidas;
    private int frutas;

    public enum State {FALLING, JUMPING, STANDING, RUNNING}

    public State currentState;
    public State previousState;
    private float stateTimer;

    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private boolean runningRight;

    private Sprite minecart;
    private Sprite tabaco;
    private Sprite minecart_back;

    private Texture textura_tabaco;
    private Texture textura_minecart;
    private Texture textura_minecart_back;

    private Vector2 posicionActual;

    public boolean reaparecer;

    public CheckPoint ultimoCheckPoint;

    //test contacto
    public boolean enSuelo;
    public boolean enRampa;

    //variables de salto
    private boolean isJumping;
    private float jumpTimer;
    private final float MAX_JUMP_TIME = 1f; // maximum time for jump boost (in seconds)
    private final float INITIAL_JUMP_FORCE = 3.0f; // initial jump impulse
    private final float JUMP_BOOST = 3.0f; // continuous upward force while holding

    //STATES en principio no los voy a usar pero los dejo por que pueden venir bien
    public Kart(World world) {
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
//        this.screen = screen;
        enSuelo = true;
        enRampa = false;
        reaparecer = false;
        ultimoCheckPoint = null;

        textura_tabaco = new Texture("marlboro_64.png");
        textura_minecart = new Texture("minecart_64.png");
        textura_minecart_back = new Texture("minecart_64_back.png");

        minecart = new Sprite(textura_minecart);
        tabaco = new Sprite(textura_tabaco);
        minecart_back = new Sprite(textura_minecart_back);

        this.set(minecart);
        this.setBounds(0, 0, 32 / MineKart.PPM, 32 / MineKart.PPM);
        tabaco.setBounds(0, 0, this.getWidth(), this.getHeight());
        minecart_back.setBounds(0, 0, this.getWidth(), this.getHeight());

        runningRight = true;
        defineKart();

        posicionActual = new Vector2(b2Body.getPosition().x - minecart.getWidth() / 2, b2Body.getPosition().y - minecart.getHeight() / 2);

        cantidad_monedas = 0;
        cantidad_vidas = 4;
        frutas = 0;
    }

    // propiedades fisicas
    public void defineKart() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(200 / MineKart.PPM, 270 / MineKart.PPM); // TODO posicion de inicio de cada nivel
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(9 / MineKart.PPM);

        fDef.shape = circleShape;
        fDef.friction = 0.1f;
        b2Body.createFixture(fDef).setUserData(this);
        circleShape.dispose();
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

    public int getFrutas() {
        return frutas;
    }

    public void setFrutas(int frutas) {
        this.frutas = frutas;
    }

    public void setCantidad_monedas(int cantidad_monedas) {
        this.cantidad_monedas = cantidad_monedas;
    }

    public int getCantidad_monedas() {
        return cantidad_monedas;
    }

    public int getCantidad_vidas() {
        return cantidad_vidas;
    }

    public void setCantidad_vidas(int cantidad_vidas) {
        this.cantidad_vidas = cantidad_vidas;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        posicionActual.lerp(new Vector2(x, y), .45f);
    }

    @Override
    public void draw(Batch batch) {
        minecart_back.draw(batch);
        tabaco.draw(batch);
        super.draw(batch);
    }

    public void kartInput(float dt) {
        // Jump start
        if ((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.justTouched()) && enSuelo) {
            isJumping = true;
            jumpTimer = 0;
            // Initial jump impulse
            b2Body.setLinearVelocity(b2Body.getLinearVelocity().x, 0); // Reset vertical velocity
            b2Body.applyLinearImpulse(new Vector2(0, INITIAL_JUMP_FORCE), b2Body.getWorldCenter(), true);
        }

        // Jump continuation
        if ((Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isTouched()) && isJumping) {
            if (jumpTimer < MAX_JUMP_TIME) {
                // Apply continuous upward force while holding
                b2Body.applyForceToCenter(new Vector2(0, JUMP_BOOST), true);
            }
        } else {
            // If button/touch released, stop jumping
            isJumping = false;
        }
    }

    public void update(float dt) {
        // Update jump timer
        if (isJumping) {
            jumpTimer += dt;
            if (jumpTimer >= MAX_JUMP_TIME) {
                isJumping = false;
            }
        }

        setPosition(b2Body.getPosition().x - getWidth() / 2, (b2Body.getPosition().y - getHeight() / 2));
        tabaco.setPosition(b2Body.getPosition().x - getWidth() / 2, posicionActual.y + 5 / MineKart.PPM);
        minecart_back.setPosition(b2Body.getPosition().x - getWidth() / 2, (b2Body.getPosition().y - getHeight() / 2) + 1 / MineKart.PPM);

        // velocidad constante FIXME rompe las rampas (ahora menos)
        if (!enRampa) {
            b2Body.setLinearVelocity(new Vector2(1.5f, b2Body.getLinearVelocity().y));
        }

        // TODO muerte por caida revisar
        if (b2Body.getPosition().y < 0) {
            setCantidad_vidas(getCantidad_vidas() - 1);
            reaparecer = true;
        }

        kartInput(dt);
    }

}
