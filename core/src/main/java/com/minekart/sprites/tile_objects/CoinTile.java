package com.minekart.sprites.tile_objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.Fase;
import com.minekart.screens.Nivel;
import com.minekart.sprites.Kart;

public class CoinTile extends InteractiveTileObject {
    public CoinTile(World world, TiledMap map, Rectangle bounds, Nivel screen) {
        super(world, map, bounds, screen);
        fixture.setUserData(this);
    }

    public void onCollision(Kart player) {
        player.setPuntuacion(player.getPuntuacion() + MineKart.COIN_VALUE);
        player.setCantidad_monedas(player.getCantidad_monedas() + 1);
        getCell().setTile(null);
        screen.removeBody(body);
    }
}
