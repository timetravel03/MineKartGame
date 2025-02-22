package com.minekart.sprites.tile_objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.Fase;
import com.minekart.screens.Nivel;
import com.minekart.sprites.Kart;

public class FruitTile extends InteractiveTileObject {
    public FruitTile(World world, TiledMap map, Rectangle bounds, Nivel screen) {
        super(world, map, bounds, screen);
        fixture.setUserData(this);
        body.setLinearVelocity(new Vector2(0, -1f));

    }

    @Override
    public void onCollision(Kart player) {
        player.setPuntuacion(player.getPuntuacion() + MineKart.FRUIT_VALUE);
        player.setFrutas(player.getFrutas() + 1);
        getCell().setTile(null);
        screen.removeBody(body);
    }
}
