package com.minekart.screens.niveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.minekart.MineKart;
import com.minekart.screens.PantallaMuerte;

public class TercerNivel extends Nivel{
    public TercerNivel(MineKart game) {
        super(game);
        b2dr = new Box2DDebugRenderer();
        id = 3;
        finalLevel = true;

        music = Gdx.audio.newMusic(Gdx.files.internal("music/lv3.wav"));
    }

    @Override
    public void show() {
        super.show();
        music.play();
        music.setLooping(true);
        music.setVolume(MineKart.volume);
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
        worldCreator.crearFrutas();
    }

    @Override
    void renderizarExtra(SpriteBatch batch) {

    }
}
