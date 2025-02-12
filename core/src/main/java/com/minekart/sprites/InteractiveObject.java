package com.minekart.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.PlayScreen;

public abstract class InteractiveObject extends Sprite{
    public Body body;
    private World world;
    public boolean recogida;

    public InteractiveObject(World world, Vector2 pos, PlayScreen screen, Texture texture) {
        this.world = world;
        recogida = false;
        this.set(new Sprite(texture));
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(pos);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor= true; // si es sensor no colisiona
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6/MineKart.PPM);
        fixtureDef.shape = circleShape;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
    }

    public abstract void touched(Kart player);
}
