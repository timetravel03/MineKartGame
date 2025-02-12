package com.minekart.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.screens.Fase;

public class GroundObstacle extends InteractiveTileObject {
    boolean tocado;
    Fase rm;
    public GroundObstacle(World world, TiledMap map, Rectangle bounds, Fase screen){
        super(world,map,bounds, screen);
        fixture.setUserData(this);
        rm = screen;
    }

    // método que hace desaparecer el bloque con el que colisiona el personaje
    public void onCollision(Kart player) {
        Gdx.app.log("OBSTACLE","Collision");
        getCell().setTile(null);
        player.setCantidad_vidas(player.getCantidad_vidas()-1);
        rm.removeBody(body);
    }

}
