package com.minekart.sprites.interactive_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.Kart;

public abstract class InteractiveObject extends Sprite{
    protected Body body;
    protected World world;
    protected boolean contacto;
    protected Nivel screen;

    public InteractiveObject(World world, Vector2 pos, Nivel screen, Texture texture) {
        this.world = world;
        this.screen = screen;
        contacto = false;
        this.set(new Sprite(texture));
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(pos);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor= true; // si es sensor no colisiona
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(10/MineKart.PPM);
        fixtureDef.shape = circleShape;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
    }

    public abstract void touched(Kart player);
}
