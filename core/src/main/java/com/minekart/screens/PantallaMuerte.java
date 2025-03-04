package com.minekart.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.minekart.MineKart;

public class PantallaMuerte extends ScreenAdapter {
    private Stage stage;
    private TextField inputField;
    private Skin skin;
    private TextButton acceptButton;

    public PantallaMuerte(MineKart game, int puntuacion_nivel) {
        stage = new Stage(new FitViewport(800, 480));
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);

        inputField = new TextField("", skin);
        acceptButton = new TextButton("OK", skin);

        table.add(new Label("Has muerto", skin)).padBottom(10f).row();
        table.add(new Label("Tu puntuación es: " + (puntuacion_nivel + game.puntacionActual), skin)).padBottom(10f).row();
        table.add(new Label("Introduce tu nombre:", skin)).padBottom(10f).row();
        table.add(inputField).width(200).padBottom(10f).row();
        table.add(acceptButton).width(200).height(50).padBottom(10f).row();


        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                guardarPuntuacion(inputField.getText(), game.puntacionActual + puntuacion_nivel);
                game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    public void guardarPuntuacion(String nombre, int puntuacion) {
        if (nombre.equals("")) {
            nombre = "ANONIMO";
        }

        Preferences prefs = Gdx.app.getPreferences("minekart-preferences");

        prefs.putString("last-player", nombre);

        int score = puntuacion;
        prefs.putInteger(nombre + "-score", score);

        String highScores = prefs.getString("highscores", "");
        prefs.putString("highscores", highScores + nombre + ":" + score + ",");

        prefs.flush();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
