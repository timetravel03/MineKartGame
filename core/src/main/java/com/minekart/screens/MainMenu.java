package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
import com.minekart.screens.niveles.PrimerNivel;

public class MainMenu extends ScreenAdapter {
    private Stage stage;
    private Table table;
    private Viewport viewport;
    private Label titulo;

    // aspecto
    private Skin skin;

    // menu principal
    private TextButton jugarButton;
    private TextButton salirButton;
    private TextButton opcionesButton;

    //opciones
    private Table tableOpciones;
    private Slider sliderVolumen;
    private TextButton volumenButton;
    private TextButton vibracionButton;
    private TextButton idiomaButton;
    private TextButton creditosButton;
    private TextButton atrasButton;

    MineKart game;

    //TODO implementar funcionalidad completa del menú
    public MainMenu(MineKart game) {
        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        table = new Table();
        this.game = game;

        tableOpciones = new Table();
        tableOpciones.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        table.setFillParent(true);

        //menu principal
        jugarButton = new TextButton("JUGAR", skin);
        jugarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Jugar");
                game.setScreen(new PrimerNivel(game));
                dispose();
            }
        });

        opcionesButton = new TextButton("OPCIONES", skin);
        opcionesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Opciones");
                jugarButton.setVisible(false);
                salirButton.setVisible(false);
                opcionesButton.setVisible(false);
            }
        });

        salirButton = new TextButton("SALIR", skin);
        salirButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Salir");
                Gdx.app.exit();
            }
        });

        //opciones
        sliderVolumen = new Slider(0, 100, 1, false, skin);
        vibracionButton = new TextButton("VIBRACION", skin);
        idiomaButton = new TextButton("IDIOMA", skin);

        titulo = new Label("MineKartGame", skin);

        table.add(titulo).padBottom(20f).row();
        table.add(jugarButton).width(200).height(50).padBottom(10f).row();
        table.add(opcionesButton).width(200).height(50).padBottom(10f).row();
        table.add(salirButton).width(200).height(50).row();
        table.center();


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
