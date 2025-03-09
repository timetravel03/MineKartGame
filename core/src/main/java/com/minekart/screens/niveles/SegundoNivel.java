package com.minekart.screens.niveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.minekart.MineKart;

public class SegundoNivel extends Nivel{
    public SegundoNivel(MineKart game) {
        super(game);
        b2dr = new Box2DDebugRenderer();
        id = 2;
        // FIXME no se puede instaciar un nivel anterior, crashea
        siguienteNivel = new TercerNivel(game);
        music = Gdx.audio.newMusic(Gdx.files.internal("music/lv2.wav"));
    }

    @Override
    public void show() {
        super.show();
        music.play();
        music.setLooping(true);
        music.setVolume(MineKart.volume*.7f);
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
        worldCreator.crearFrutas();
        worldCreator.crearMonedas();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            kartPlayer.b2Body.setTransform(70, 3, 0);
        }
    }

    @Override
    void renderizarExtra(SpriteBatch batch) {

    }
}
