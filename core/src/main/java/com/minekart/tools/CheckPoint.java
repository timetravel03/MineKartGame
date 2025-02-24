package com.minekart.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.screens.Nivel;
import com.minekart.sprites.Kart;
import com.minekart.sprites.interactive_objects.InteractiveObject;
import com.minekart.sprites.tile_objects.InteractiveTileObject;

public class CheckPoint extends InteractiveTileObject {
    public int id;
    public boolean cruzado;

    public CheckPoint(int id, World world, TiledMap map, Rectangle bounds, Nivel screen) {
        super(world, map, bounds, screen);
        this.id = id;
        cruzado = false;
        fixture.setUserData(this);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public void onCollision(Kart player) {
        player.ultimoCheckPoint = this;
    }
}
