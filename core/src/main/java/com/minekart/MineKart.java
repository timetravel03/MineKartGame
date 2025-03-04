package com.minekart;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.minekart.screens.MainMenu;

import java.util.Locale;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
//TODO revisar el tema del delta time, si se ejecuta a mas de 60fps va demasiado rapido
public class MineKart extends Game {
    public SpriteBatch batch;

    // alto
    public static final int V_WIDTH = 600;

    // ancho
    public static final int V_HEIGHT = 385;

    // pixeles por metro
    public static final float PPM = 100;

    // valor de las monedas
    public static final int COIN_VALUE = 100;

    // valor de las frutas
    public static final int FRUIT_VALUE = 500;

    // si esta activada o no la vibracion
    public static boolean vibracion;

    // puntacion de la "run" actual, se reinicia al morir
    public static int puntacionActual;

    public static final FileHandle baseFileHandle = new FileHandle("i18n/minekart_lang");
    public static Locale locale;
    public static I18NBundle myBundle;

    @Override
    public void create() {
        puntacionActual = 0;
        vibracion = true;
        batch = new SpriteBatch();
        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
