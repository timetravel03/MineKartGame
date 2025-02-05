package com.minekart.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.minekart.sprites.Coin;
import com.minekart.sprites.InteractiveTileObject;
import com.minekart.sprites.Kart;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("BEGIN CONTACT", "");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
//        if (fixA.getUserData() == "front" || fixB.getUserData() == "front") {
//            Fixture front = fixA.getUserData() == "front" ? fixA : fixB;
//            Fixture object = front == fixA ? fixB : fixA;
//
//            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
//                ((InteractiveTileObject) object.getUserData()).onHeadHit();
//            }
//        }
        if ((fixA.getUserData() instanceof Coin || fixB.getUserData() instanceof Coin)){
            Coin c = fixA.getUserData() instanceof Coin ? (Coin) fixA.getUserData() : (Coin) fixB.getUserData();
            Kart k = fixA.getUserData() instanceof Kart ? (Kart)fixA.getUserData() : (Kart)fixB.getUserData();
            c.coinTouched(k);
        }

    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("END CONTACT", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
