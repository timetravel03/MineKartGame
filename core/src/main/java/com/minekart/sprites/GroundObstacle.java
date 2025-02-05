package com.minekart.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;

public class GroundObstacle extends InteractiveTileObject {
    public GroundObstacle(World world, TiledMap map, Rectangle bounds){
        super(world,map,bounds);
        fixture.setUserData(this);
        setCategoryFilter(MineKart.OBSTACLE_BIT);
    }

    // método que hace desaparecer el bloque con el que colisiona el personaje
    @Override
    public void onHeadHit() {
        Gdx.app.log("OBSTACLE","Collision");
        setCategoryFilter(MineKart.DESTROYED_BIT);
        getCell().setTile(null);
    }
}
