package com.minekart.screens.niveles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.minekart.MineKart;

public class TercerNivel extends Nivel{
    public TercerNivel(MineKart game) {
        super(game);
        b2dr = new Box2DDebugRenderer();
        id = 3;
//        siguienteNivel = new PrimerNivel(game);
    }
    @Override
    void cargarAssets() {
        map = mapLoader.load("mapa_3.tmx");
    }

    @Override
    void cargarColisiones() {
        worldCreator.crearSuelo();
        worldCreator.crearMonedas();
        worldCreator.crearCheckPoints();
        worldCreator.crearRebotes();
    }

    @Override
    void renderizarExtra(SpriteBatch batch) {

    }
}
