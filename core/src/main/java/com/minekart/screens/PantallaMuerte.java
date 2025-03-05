package com.minekart.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
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

// Pantalla que se muestra cuando el jugador muere, incluye un campo de texto para introducir el nombre del jugador, se reutiliza para la pantalla de ganar
public class PantallaMuerte extends ScreenAdapter {
    private Stage stage;
    private TextField inputField;
    private Skin skin;
    private TextButton acceptButton;
    private Music deathMusic;

    public PantallaMuerte(MineKart game, int puntuacion_nivel, boolean finalLevel, boolean completado) {
        stage = new Stage(new FitViewport(800, 480));
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        if (finalLevel && completado) {
            deathMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/pack_sound.ogg"));
        } else {
            deathMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/death_jingle.ogg"));
        }
        deathMusic.setLooping(false);
        deathMusic.setVolume(MineKart.volume);

        Table table = new Table();
        table.setFillParent(true);

        inputField = new TextField("", skin);   // TODO el teclado cubre toda la pantalla
        acceptButton = new TextButton("OK", skin);

        if (finalLevel && completado) {
            table.add(new Label(MineKart.myBundle.get("youWin"), skin)).padBottom(10f).row();
        } else {
            table.add(new Label(MineKart.myBundle.get("youDied"), skin)).padBottom(10f).row();
        }
        table.add(new Label(MineKart.myBundle.get("score") + ":" + (puntuacion_nivel + game.puntacionActual), skin)).padBottom(10f).row();
        table.add(new Label(MineKart.myBundle.get("inputName") + ":", skin)).padBottom(10f).row();
        table.add(inputField).width(200).padBottom(10f).row();
        table.add(acceptButton).width(200).height(50).padBottom(10f).row();

        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                guardarPuntuacion(inputField.getText(), MineKart.puntacionActual + puntuacion_nivel);
                deathMusic.stop();
                game.setScreen(new MainMenu(game));
                MineKart.puntacionActual = 0;
            }
        });

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    // Guarda la puntuación del jugador en las preferencias del juego (revisar)
    public void guardarPuntuacion(String nombre, int puntuacion) {
        Preferences prefs = Gdx.app.getPreferences("minekart-preferences");

        if (nombre.equals("")) {
            nombre = MineKart.myBundle.get("anon");
        }
        prefs.putString("last-player", nombre);
        prefs.putInteger(nombre + "-score", puntuacion);

        String highScores = prefs.getString("highscores", "");
        prefs.putString("highscores", highScores + nombre + ":" + puntuacion + ",");

        // guarda las preferencias
        prefs.flush();
    }

    @Override
    public void show() {
        super.show();
        deathMusic.play();
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
        deathMusic.dispose();
    }
}
