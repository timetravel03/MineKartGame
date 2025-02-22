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

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class MineKart extends Game {
    public SpriteBatch batch;
    public static final int V_WIDTH = 600;
    public static final int V_HEIGHT = 385;
    public static final float PPM = 100;
    public static final short DEFAULT_BIT = 1;
    public static final short KART_BIT = 2;
    public static final short OBSTACLE_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final int COIN_VALUE = 100;
    public static final int FRUIT_VALUE = 500;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
