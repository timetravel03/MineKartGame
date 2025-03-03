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
    private int puntuacion;
    private int cantidad_monedas;
    private int cantidad_vidas;
    private int frutas;

    private Sprite minecart;
    private Sprite tabaco;

    private Texture textura_tabaco;
    private Texture textura_minecart;

    private Vector2 posicionActual;

    public boolean reaparecer;

    public CheckPoint ultimoCheckPoint;

    private Vector2 posicionInicial;

    //test contacto
    public boolean enSuelo;
    public boolean enRampa;

    //variables de salto
    private boolean isJumping;
    private float jumpTimer;
    private final float MAX_JUMP_TIME = 1f; // timepo maximo de impulso (en segundos)
    private final float INITIAL_JUMP_FORCE = 3.0f; // impulso inicial de salto
    private final float JUMP_BOOST = 3.0f; // fuerza continua hacia arriba mientras esta pulsado

    // TODO revisar bordes de hitboxes
    public Kart(World world) {
        this.world = world;
        enSuelo = true;
        enRampa = false;
        reaparecer = false;
        ultimoCheckPoint = null;

        textura_tabaco = new Texture("junimo_copia_d.png");
        textura_minecart = new Texture("minekart.png");

        minecart = new Sprite(textura_minecart);
        tabaco = new Sprite(textura_tabaco);

        this.set(minecart);
        this.setBounds(0, 0, 36 / MineKart.PPM, 36 / MineKart.PPM);
        tabaco.setBounds(0, 0, this.getWidth(), this.getHeight());

        defineKart();

        posicionActual = new Vector2(b2Body.getPosition().x - minecart.getWidth() / 2, b2Body.getPosition().y - minecart.getHeight() / 2);

        cantidad_monedas = 0;
        cantidad_vidas = 4;
        frutas = 0;
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    // propiedades fisicas
    public void defineKart() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(0, 0);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(9 / MineKart.PPM);

        fDef.shape = circleShape;
        fDef.friction = 0.1f;
        b2Body.createFixture(fDef).setUserData(this);
        circleShape.dispose();
        enSuelo = false;
    }

    @Override
    public void setRotation(float degrees) {
        super.setRotation(degrees);
        minecart.setRotation(degrees);
    }

    public void setPosicionInicial(Vector2 position) {
        b2Body.setTransform(position, 0);
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
        tabaco.draw(batch);
        super.draw(batch);
    }

    // este metodo se usa en el HUD ya que el resto de imputs estan ahí, pero podria estar en la clase Nivel
    public void kartInput(boolean justPressed, boolean isTouched) {

        // logica del salto
        if (justPressed && enSuelo && !isJumping) {
            isJumping = true;
            jumpTimer = 0;
            // impulso inicial
            b2Body.setLinearVelocity(b2Body.getLinearVelocity().x, 0); // resetear velocidad en y
            b2Body.applyLinearImpulse(new Vector2(0, INITIAL_JUMP_FORCE), b2Body.getWorldCenter(), true);
        }

        // Jump continuation
        if (isTouched && isJumping) {
            if (jumpTimer < MAX_JUMP_TIME) {
                // aplicar fuerza continua miientras haya tiempo y esta pulsado
                b2Body.applyForceToCenter(new Vector2(0, JUMP_BOOST), true);
            }
        } else {
            // si se suelta el btn de salto dejar de saltar
            isJumping = false;
        }

    }

    public void update(float dt, boolean justPressed, boolean isTouched) {
        // update jump timer
        if (isJumping) {
            jumpTimer += dt;
            if (jumpTimer >= MAX_JUMP_TIME) {
                isJumping = false;
            }
        }

        // update sprite
        setPosition(b2Body.getPosition().x - getWidth() / 2, (b2Body.getPosition().y - getHeight() / 2));
        tabaco.setPosition(b2Body.getPosition().x - getWidth() / 2, posicionActual.y + 5 / MineKart.PPM);

        // velocidad constante si no esta en rampa (para conseguir inercia)
        if (!enRampa) {
            b2Body.setLinearVelocity(new Vector2(1.5f, b2Body.getLinearVelocity().y));
        }

        // rotar el personaje
        float rotation = 0;
        if (isJumping || (enRampa && !enSuelo)) {
            rotation = 30;
        } else if (enRampa && enSuelo) {
            rotation = -30;
        }
        setRotation(rotation);

        // muerte por caida
        if (b2Body.getPosition().y < 0) {
            setCantidad_vidas(getCantidad_vidas() - 1);
            reaparecer = true;
        }
    }

}
