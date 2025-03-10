package com.minekart.sprites.tile_objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.Kart;

// De esta clase hereadan las clases CoinTile, FruitTile, GroundObstacle y Meta
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected Nivel screen;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds, Nivel screen) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        this.screen = screen;

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set((bounds.getX() + bounds.getWidth() / 2) / MineKart.PPM, (bounds.getY() + bounds.getHeight() / 2) / MineKart.PPM);

        body = world.createBody(bDef);
        shape.setAsBox((bounds.getWidth() / 2) / MineKart.PPM, (bounds.getHeight() / 2) / MineKart.PPM);
        fDef.shape = shape;
        fDef.isSensor = true;
        fixture = body.createFixture(fDef);
    }

    public abstract void onCollision(Kart player);

    // metodo que obtiene la celda en la que está el personaje
    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("gfx");
        int x = (int) (body.getPosition().x * MineKart.PPM / 16);
        int y = (int) (body.getPosition().y * MineKart.PPM / 16);
        System.out.println("Pos: " + x + ", " + y);
        return layer.getCell((int) (body.getPosition().x * MineKart.PPM / 16), (int) (body.getPosition().y * MineKart.PPM / 16));
    }
}
