package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;

public class PantallaResultados implements Screen {
    private int monedas;
    private int frutas;
    private int vidas;
    private int puntuacion;
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private MineKart game;
    private Label puntosLabel;
    private Label monedasLabel;
    private Label frutasLabel;
    private Label vidasLabel;

    public PantallaResultados(MineKart game, int monedas, int frutas, int vidas) {
        this.monedas = monedas;
        this.frutas = frutas;
        this.vidas = vidas;
        this.game = game;
        this.puntuacion = monedas * 10 + frutas * 20 + vidas * 50;

        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        table = new Table();
        this.game = game;

        table.center();
        table.setFillParent(true);

        vidasLabel = new Label(String.format("Vidas: %d", vidas), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        frutasLabel = new Label(String.format("Frutas: %d", frutas), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        monedasLabel = new Label(String.format("Monedas: %d", monedas), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        puntosLabel = new Label(String.format("Puntuación: %d", puntuacion), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(monedasLabel);
        table.row();
        table.add(frutasLabel);
        table.row();
        table.add(vidasLabel);
        table.row();
        table.add(puntosLabel);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        if (Gdx.input.isTouched()) {
            this.dispose();
            game.setScreen(new PrimerNivel(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
