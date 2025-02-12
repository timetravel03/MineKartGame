package com.minekart.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.Fase;

public class FruitTile extends InteractiveTileObject {
    public FruitTile(World world, TiledMap map, Rectangle bounds, Fase screen) {
        super(world, map, bounds, screen);
        fixture.setUserData(this);
    }

    @Override
    public void onCollision(Kart player) {
        player.setPuntuacion(player.getPuntuacion() + MineKart.FRUIT_VALUE);
        player.setFrutas(player.getFrutas() + 1);
        getCell().setTile(null);
        screen.removeBody(body);
    }
}
