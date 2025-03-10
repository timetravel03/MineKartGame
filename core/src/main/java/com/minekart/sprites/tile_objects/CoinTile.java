package com.minekart.sprites.tile_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.Kart;

public class CoinTile extends InteractiveTileObject {
    public CoinTile(World world, TiledMap map, Rectangle bounds, Nivel screen) {
        super(world, map, bounds, screen);
        fixture.setUserData(this);
    }

    public void onCollision(Kart player) {
        MineKart.sounds.get("coin").play(MineKart.fxVolume);
        if (MineKart.vibracion) Gdx.input.vibrate(50);
        player.setPuntuacion(player.getPuntuacion() + MineKart.COIN_VALUE);
        player.setCantidad_monedas(player.getCantidad_monedas() + 1);
        getCell().setTile(null);
        screen.removeBody(body);
    }
}
