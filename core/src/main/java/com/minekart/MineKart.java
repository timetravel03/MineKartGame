package com.minekart;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.minekart.screens.MainMenu;

import java.util.Hashtable;
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

    // localizacion
    public static FileHandle baseFileHandle;
    public static Locale locale;
    public static I18NBundle myBundle;
    public static final Locale SPANISH_LOCALE = new Locale("es");

    public static void inicializarBundle() {
        if (locale == null) {
            // fuerza el juego al español, independientemente del idioma del sistema, en el menú se puede cambiar
            myBundle = I18NBundle.createBundle(baseFileHandle, SPANISH_LOCALE);
        } else {
            myBundle = I18NBundle.createBundle(baseFileHandle, locale);
        }
    }

    //sonidos
    public static final Hashtable<String, Sound> sounds = new Hashtable<String, Sound>();
    public static float volume = 0.5f;
    public static float fxVolume = volume*0.2f;


    @Override
    public void create() {
        baseFileHandle = Gdx.files.internal("i18n/minekart_lang");
        sounds.put("coin", Gdx.audio.newSound(Gdx.files.internal("sounds/coin_sound.ogg")));
        sounds.put("pack", Gdx.audio.newSound(Gdx.files.internal("sounds/pack_sound.ogg")));
        sounds.put("jump", Gdx.audio.newSound(Gdx.files.internal("sounds/jump.ogg")));
        sounds.put("rebote", Gdx.audio.newSound(Gdx.files.internal("sounds/rebote.ogg")));
        sounds.put("checkpoint", Gdx.audio.newSound(Gdx.files.internal("sounds/checkpoint1.ogg")));
        sounds.put("meta", Gdx.audio.newSound(Gdx.files.internal("sounds/meta.ogg")));
        sounds.put("death", Gdx.audio.newSound(Gdx.files.internal("sounds/death_jingle.ogg")));
        sounds.put("hit", Gdx.audio.newSound(Gdx.files.internal("sounds/hit.ogg")));
        sounds.put("pause", Gdx.audio.newSound(Gdx.files.internal("sounds/pause.ogg")));
        inicializarBundle();
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
