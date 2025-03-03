package com.minekart;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.minekart.screens.MainMenu;
import com.minekart.screens.PlayScreen;
import com.minekart.screens.niveles.Nivel;
import com.minekart.screens.niveles.PrimerNivel;
import com.minekart.screens.niveles.SegundoNivel;
import com.minekart.screens.niveles.TercerNivel;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
//TODO revisar el tema del delta time, si se ejecuta a mas de 60fps va demasiado rapido
public class MineKart extends Game {
    public SpriteBatch batch;
    public static final int V_WIDTH = 600;
    public static final int V_HEIGHT = 385;
    public static final float PPM = 100;
    public static final int COIN_VALUE = 100;
    public static final int FRUIT_VALUE = 500;
    public static boolean vibracion;
    @Override
    public void create() {
        vibracion = true;
        batch = new SpriteBatch();
        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
