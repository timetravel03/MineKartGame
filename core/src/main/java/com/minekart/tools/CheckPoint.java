package com.minekart.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.Kart;
import com.minekart.sprites.tile_objects.InteractiveTileObject;

public class CheckPoint extends InteractiveTileObject {
    public int id;
    public boolean cruzado;

    public CheckPoint(int id, World world, TiledMap map, Rectangle bounds, Nivel screen) {
        super(world, map, bounds, screen);
        this.id = id;
        this.bounds = bounds;
        cruzado = false;
        fixture.setUserData(this);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Vector2 puntoMasBajo() {
        return new Vector2(bounds.getX() / MineKart.PPM, (bounds.getY() + 16) / MineKart.PPM);
    }

    @Override
    public TiledMapTileLayer.Cell getCell() {
        //Descubre el tile que está en la capa inferior de la celda en la que se encuentra el checkpoint
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("gfx");
        return layer.getCell((int) (bounds.getX() / 16), (int) ((bounds.getY() / 16) + 1));
    }

    @Override
    public void onCollision(Kart player) {
        player.ultimoCheckPoint = this;
        getCell().setTile(null);
    }
}
