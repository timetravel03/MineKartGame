package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.minekart.MineKart;
import com.minekart.sprites.interactive_objects.FallingRock;
import com.minekart.tools.FuncionesNivel;

public class PrimerNivel extends Nivel {
    private Array<FallingRock> rocas;
    private float intevaloRocas = MathUtils.random(3f, 7f);
    Music music = Gdx.audio.newMusic(Gdx.files.internal("song1.wav"));

    public PrimerNivel(MineKart game) {
        super(game);
        rocas = new Array<FallingRock>();
        b2dr = new Box2DDebugRenderer();
        music.play();
        music.setLooping(true);
        music.setVolume(0.1f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // crear rocas
        deltaTimer += delta;
        if (deltaTimer > intevaloRocas) {
            deltaTimer = 0;
            intevaloRocas = MathUtils.random(3f, 7f);
            FuncionesNivel.crearRocas(gameCam, rocas, textureHashtable.get("rock"), world, this);
        }

        // update rocas
        for (int i = rocas.size - 1; i >= 0; i--) {
            rocas.get(i).update(delta);
            if (rocas.get(i).eliminar) {
                rocas.removeIndex(i);
            }
        }

        // debug
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            kartPlayer.b2Body.setTransform(70, 3, 0);
        }

        if (completado){
            music.stop();
        }
    }

    @Override
    void cargarAssets() {
        map = mapLoader.load("mapa_1.tmx");
        textureHashtable.put("rock", new Texture("rocas/rock3.png"));
    }

    @Override
    void cargarColisiones() {
//        worldCreator.crearTodo();
        worldCreator.crearSuelo();
        worldCreator.crearMonedas();
        worldCreator.crearCheckPoints();
        worldCreator.crearObstaculos();
        worldCreator.crearFrutas();
    }

    @Override
    void renderizarExtra(SpriteBatch batch) {
        for (FallingRock r:
            rocas) {
            r.draw(game.batch);
        }
    }
}
