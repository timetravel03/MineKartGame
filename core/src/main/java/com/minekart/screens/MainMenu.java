package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
import com.minekart.screens.niveles.PrimerNivel;

public class MainMenu extends ScreenAdapter {
    private Stage stage;
    private Viewport viewport;
    private Label titulo;
    private Label jugar;
    private TextButton jugarButton;
    private TextButton salirButton;
    private TextButton opcionesButton;
    private Slider slider;
    private Table table;
    private Skin skin;

    MineKart game;

    //TODO implementar funcionalidad completa del menú
    public MainMenu(MineKart game) {
        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        table = new Table();
        this.game = game;
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        table.setFillParent(true);

        jugarButton = new TextButton("JUGAR", skin);
        salirButton = new TextButton("SALIR", skin);
        opcionesButton = new TextButton("OPCIONES", skin);

        slider = new Slider(0, 100, 1, false, skin);

        titulo = new Label("MineKartGame", skin);

        table.add(titulo).padBottom(20f).row();
        table.add(jugarButton).width(200).height(50).padBottom(10f).row();
        table.add(opcionesButton).width(200).height(50).padBottom(10f).row();
        table.add(salirButton).width(200).height(50).row();
        table.add(slider).width(200).height(50).row();
        table.center();

        jugarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Jugar");
                game.setScreen(new PrimerNivel(game));
                dispose();
            }
        });

        salirButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Salir");
                Gdx.app.exit();
            }
        });

        opcionesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Opciones");
            }
        });

        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(delta);
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
