package com.minekart.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.minekart.sprites.interactive_objects.InteractiveObject;
import com.minekart.sprites.tile_objects.InteractiveTileObject;
import com.minekart.sprites.Kart;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // con que sea instancia de la clase vale jejeje
        if ((fixA.getUserData() instanceof InteractiveTileObject || fixB.getUserData() instanceof InteractiveTileObject)) {
            InteractiveTileObject g = fixA.getUserData() instanceof InteractiveTileObject ? (InteractiveTileObject) fixA.getUserData() : (InteractiveTileObject) fixB.getUserData();
            Kart k = fixA.getUserData() instanceof Kart ? (Kart) fixA.getUserData() : (Kart) fixB.getUserData();
            g.onCollision(k);
            Gdx.app.log("BEGIN CONTACT", "InteractiveTileObject");
        }
        if ((fixA.getUserData() instanceof InteractiveObject || fixB.getUserData() instanceof InteractiveObject)) {
            InteractiveObject g = fixA.getUserData() instanceof InteractiveObject ? (InteractiveObject) fixA.getUserData() : (InteractiveObject) fixB.getUserData();
            Kart k = fixA.getUserData() instanceof Kart ? (Kart) fixA.getUserData() : (Kart) fixB.getUserData();
            g.touched(k);
            Gdx.app.log("BEGIN CONTACT", "InteractiveObject");
        }

        if ((fixA.getUserData() == "rampa" || fixB.getUserData() == "rampa")) {
            Kart k = fixA.getUserData() instanceof Kart ? (Kart) fixA.getUserData() : (Kart) fixB.getUserData();
            k.enRampa = true;
            k.enSuelo = true;
            Gdx.app.log("BEGIN CONTACT", "Rampa");
        }
        if ((fixA.getUserData() == "suelo" || fixB.getUserData() == "suelo")) {
            Kart k = fixA.getUserData() instanceof Kart ? (Kart) fixA.getUserData() : (Kart) fixB.getUserData();
            k.enSuelo = true;
            k.enRampa = false;
            Gdx.app.log("BEGIN CONTACT", "Suelo");
        }
        //checkpoints
        if ((fixA.getUserData() instanceof CheckPoint || fixB.getUserData() instanceof CheckPoint)) {
            CheckPoint c = fixA.getUserData() instanceof CheckPoint ? (CheckPoint) fixA.getUserData() : (CheckPoint) fixB.getUserData();
            Kart k = fixA.getUserData() instanceof Kart ? (Kart) fixA.getUserData() : (Kart) fixB.getUserData();
            c.onCollision(k);
            Gdx.app.log("BEGIN CONTACT", "CheckPoint");
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if ((fixA.getUserData() == "suelo" || fixB.getUserData() == "suelo")) {
            Kart k = fixA.getUserData() instanceof Kart ? (Kart) fixA.getUserData() : (Kart) fixB.getUserData();
            k.enSuelo = false;
            Gdx.app.log("END CONTACT", "Suelo");
        }
        if ((fixA.getUserData() == "rampa" || fixB.getUserData() == "rampa")) {
            Kart k = fixA.getUserData() instanceof Kart ? (Kart) fixA.getUserData() : (Kart) fixB.getUserData();
            k.enSuelo = false;
            Gdx.app.log("END CONTACT", "Rampa");
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
