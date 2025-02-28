package com.minekart.screens.niveles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.minekart.MineKart;

public class SegundoNivel extends Nivel{
    public SegundoNivel(MineKart game) {
        super(game);
        b2dr = new Box2DDebugRenderer();
    }
    @Override
    void cargarAssets() {
        map = mapLoader.load("mapa_2.tmx");
    }

    @Override
    void cargarColisiones() {
        worldCreator.crearSuelo();
        worldCreator.crearRampas();
        worldCreator.crearCheckPoints();
    }

    @Override
    void renderizarExtra(SpriteBatch batch) {

    }
}
