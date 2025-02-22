package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.minekart.MineKart;
import com.minekart.sprites.interactive_objects.FallingRock;
import com.minekart.tools.Libreria;

public class PrimerNivel extends Nivel {
    private Array<FallingRock> rocas;

    public PrimerNivel(MineKart game) {
        super(game);
        rocas = new Array<FallingRock>();
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // crear rocas
        deltaTimer += delta;
        if (deltaTimer > 5f) {
            deltaTimer = 0;
            Libreria.crearRocas(gameCam, rocas, textureHashtable.get("rock"), world, this);
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
            kartPlayer.b2Body.setTransform(28, 3, 0);
        }
    }

    @Override
    void cargarAssets() {
        map = mapLoader.load("test2.tmx");
        textureHashtable.put("rock", new Texture("rocas/rock1.png"));
    }

    @Override
    void cargarColisiones() {
        worldCreator.crearTodo();
    }

    @Override
    void renderizarExtra(SpriteBatch batch) {
        for (FallingRock r:
            rocas) {
            r.draw(game.batch);
        }
    }
}
