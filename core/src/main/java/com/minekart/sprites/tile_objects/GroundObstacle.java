package com.minekart.sprites.tile_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.screens.Nivel;
import com.minekart.sprites.Kart;

public class GroundObstacle extends InteractiveTileObject {
    boolean tocado;
    Nivel screen;
    public GroundObstacle(World world, TiledMap map, Rectangle bounds, Nivel screen){
        super(world,map,bounds, screen);
        fixture.setUserData(this);
        this.screen = screen;
    }

    // método que hace desaparecer el bloque con el que colisiona el personaje
    public void onCollision(Kart player) {
        Gdx.app.log("OBSTACLE","Collision");
        getCell().setTile(null);
        player.setCantidad_vidas(player.getCantidad_vidas()-1);
        screen.removeBody(body);
    }

}
