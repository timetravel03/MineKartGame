package com.minekart.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.minekart.MineKart;
import com.minekart.screens.niveles.Nivel;

public class Eyeball extends Sprite {
    TextureAtlas eye_atlas;
    Animation<TextureRegion> eye_animation;
    Array<TextureAtlas.AtlasRegion> frames;
    Sprite the_hand;
    float stateTime;
    public Body eye_body;
    public static final int boundX = 100;
    public static final int boundY = 100;

    //OLD CODE
    public Eyeball(World world, Nivel screen) {
//        eye_atlas = screen.getAtlas();
        frames = new Array<>();
        for (int i = 0; i < 17; i++) {
            frames.add(eye_atlas.findRegion(String.format("frame_%02d_delay-0.1s", i)));
        }
        eye_animation = new Animation<>(.1f, frames, Animation.PlayMode.LOOP);
//        the_hand = new Sprite(screen.getHand_texture());
        stateTime = 0f;
        this.setRegion(eye_animation.getKeyFrame(stateTime, true));
        this.setBounds(0, 0, boundX / MineKart.PPM, boundY / MineKart.PPM);

        the_hand.setBounds(0, 0, boundX * 2.428f / MineKart.PPM, boundY * 2.428f / MineKart.PPM);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody; // cambiar, solo para testeo
        bodyDef.position.set(600 / MineKart.PPM, 256 / MineKart.PPM);
        eye_body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(boundX / MineKart.PPM);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(boundX/MineKart.PPM,boundY/MineKart.PPM);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        eye_body.createFixture(fixtureDef).setUserData(this);
//        eye_body.setLinearVelocity(new Vector2(.5f,0));
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        the_hand.draw(batch);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        the_hand.setPosition(x, y);
    }

    public TextureRegion animateEye(float dt) {
        stateTime += dt;
        return eye_animation.getKeyFrame(stateTime, true);
    }

    public void update(float dt, Kart player) {
        eye_body.setLinearVelocity(new Vector2(player.b2Body.getLinearVelocity().x, player.b2Body.getLinearVelocity().y));
        this.setRegion(animateEye(dt));
        this.setPosition(eye_body.getPosition().x - this.getWidth() / 2, eye_body.getPosition().y - this.getWidth() / 2);
        the_hand.setPosition(eye_body.getPosition().x - this.getWidth() -21 / MineKart.PPM, eye_body.getPosition().y - this.getWidth() - 85 / MineKart.PPM); //
    }
}
