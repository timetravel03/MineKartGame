package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
import com.minekart.screens.niveles.PrimerNivel;

public class MainMenu extends ScreenAdapter {
    private Stage stage;
    private Viewport viewport;
    private Label titulo;
    private Label jugar;
    private Table table;
    MineKart game;

    public MainMenu(MineKart game){
        viewport = new FitViewport(MineKart.V_WIDTH,MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        table = new Table();
        this.game = game;

        table.center();
        table.setFillParent(true);

        titulo = new Label("MineKartGame", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        jugar = new Label("JUGAR", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(titulo);
        table.row();
        table.add(jugar);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        if (Gdx.input.isTouched()){
            this.dispose();
            game.setScreen(new PrimerNivel(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
