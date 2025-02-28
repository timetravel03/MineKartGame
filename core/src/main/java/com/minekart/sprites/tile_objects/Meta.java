package com.minekart.sprites.tile_objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.Kart;

public class Meta extends InteractiveTileObject {

    public Meta(World world, TiledMap map, Rectangle bounds, Nivel screen) {
        super(world, map, bounds, screen);
        fixture.setUserData(this);
    }
    @Override
    public void onCollision(Kart player) {
        screen.completado = true;
    }
}
