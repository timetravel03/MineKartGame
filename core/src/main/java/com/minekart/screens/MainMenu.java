package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
import com.minekart.screens.niveles.PrimerNivel;
import com.minekart.screens.niveles.SegundoNivel;
import com.minekart.screens.niveles.TercerNivel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainMenu extends ScreenAdapter {
    private Stage stage;
    private Table mainTable;
    private Table optionsTable;
    private Table recordsTable;
    private Table creditsTable;
    private Table levelSelectTable;
    private Viewport viewport;
    private Label titulo;

    // aspecto
    private Skin skin;

    // menu principal
    private TextButton jugarButton;
    private TextButton salirButton;
    private TextButton opcionesButton;
    private TextButton recordsButton;

    // opciones
    private Slider sliderVolumen;
    private Label volumeLabel;
    private TextButton vibracionButton;
    private TextButton idiomaButton;
    private TextButton creditosButton;
    private TextButton atrasButton;
    private TextButton levelSelectButton;

    // records
    private Label[] recordLabels;
    private TextButton recordsBackButton;

    // selector de niveles
    private TextButton[] levelButtons;
    private TextButton levelSelectBackButton;

    // creditos
    private Label[] creditLabels;
    private TextButton creditsBackButton;

    // varios
    private MineKart game;
    private Music menuMusic;

    public MainMenu(MineKart game) {
        game.puntacionActual = 0;
        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        mainTable = new Table();
        optionsTable = new Table();
        recordsTable = new Table();
        creditsTable = new Table();
        levelSelectTable = new Table();

        this.game = game;

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/drone.ogg"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        menuMusic.play();

        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        mainTable.setFillParent(true);
        optionsTable.setFillParent(true);
        recordsTable.setFillParent(true);
        creditsTable.setFillParent(true);
        levelSelectTable.setFillParent(true);

        // ocultar todas las tablas excepto la principal
        optionsTable.setVisible(false);
        recordsTable.setVisible(false);
        creditsTable.setVisible(false);
        levelSelectTable.setVisible(false);

        setupMainMenu();
        setupOptionsMenu();
        setupRecordsMenu();
        setupLevelSelectMenu();
        setupCreditsMenu();

        stage.addActor(mainTable);
        stage.addActor(optionsTable);
        stage.addActor(recordsTable);
        stage.addActor(creditsTable);
        stage.addActor(levelSelectTable);
    }

    private void setupMainMenu() {
        titulo = new Label("MineKartGame", skin);

        jugarButton = new TextButton("JUGAR", skin);
        jugarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Jugar");
                menuMusic.stop();
                game.setScreen(new PrimerNivel(game));
                dispose();
            }
        });

        opcionesButton = new TextButton("OPCIONES", skin);
        opcionesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Opciones");
                mainTable.setVisible(false);
                optionsTable.setVisible(true);
            }
        });

        recordsButton = new TextButton("RECORDS", skin);
        recordsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Records");
                mainTable.setVisible(false);
                recordsTable.setVisible(true);
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

        mainTable.add(titulo).padBottom(20f).row();
        mainTable.add(jugarButton).width(200).height(50).padBottom(10f).row();
        mainTable.add(opcionesButton).width(200).height(50).padBottom(10f).row();
        mainTable.add(recordsButton).width(200).height(50).padBottom(10f).row();
        mainTable.add(salirButton).width(200).height(50).row();
        mainTable.center();
    }

    private void setupOptionsMenu() {
        Label optionsTitle = new Label("OPCIONES", skin);

        volumeLabel = new Label("Volumen: 50%", skin);
        sliderVolumen = new Slider(0, 100, 1, false, skin);
        sliderVolumen.setValue(50);
        sliderVolumen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                volumeLabel.setText("Volumen: " + (int) sliderVolumen.getValue() + "%");
                menuMusic.setVolume(sliderVolumen.getValue() / 100f);
            }
        });

        vibracionButton = new TextButton(MineKart.vibracion ? "VIBRAR: ON" : "VIBRAR: OFF", skin);
        vibracionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MineKart.vibracion = !MineKart.vibracion;
                vibracionButton.setText(MineKart.vibracion ? "VIBRAR: ON" : "VIBRAR: OFF");
            }
        });

        levelSelectButton = new TextButton("NIVELES", skin);
        levelSelectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsTable.setVisible(false);
                levelSelectTable.setVisible(true);
            }
        });

        idiomaButton = new TextButton("IDIOMA: ES", skin);
        idiomaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String currentText = idiomaButton.getText().toString();
                if (currentText.contains("ES")) {
                    idiomaButton.setText("IDIOMA: EN");
                } else {
                    idiomaButton.setText("IDIOMA: ES");
                }
            }
        });

        creditosButton = new TextButton("CREDITOS", skin);
        creditosButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsTable.setVisible(false);
                creditsTable.setVisible(true);
            }
        });

        atrasButton = new TextButton("ATRAS", skin);
        atrasButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsTable.setVisible(false);
                mainTable.setVisible(true);
            }
        });

        optionsTable.add(optionsTitle).padBottom(20f).row();
        optionsTable.add(volumeLabel).padBottom(5f).row();
        optionsTable.add(sliderVolumen).width(200).padBottom(10f).row();
        optionsTable.add(vibracionButton).width(200).height(50).padBottom(10f).row();
        optionsTable.add(levelSelectButton).width(200).height(50).padBottom(10f).row();
        optionsTable.add(idiomaButton).width(200).height(50).padBottom(10f).row();
        optionsTable.add(creditosButton).width(200).height(50).padBottom(10f).row();
        optionsTable.add(atrasButton).width(200).height(50).row();
        optionsTable.center();
    }

    // TODO REVISAR
    private void loadHighScores() {
        Preferences prefs = Gdx.app.getPreferences("minekart-preferences");
        String highScores = prefs.getString("highscores", "");

        // Parse high scores and display in recordLabels
        if (!highScores.isEmpty()) {
            String[] entries = highScores.split(",");

            // Create a map to store name-score pairs
            Map<String, Integer> scoresMap = new HashMap<>();

            // Parse entries and keep highest score for each name
            for (String entry : entries) {
                if (!entry.isEmpty()) {
                    String[] parts = entry.split(":");
                    if (parts.length == 2) {
                        String name = parts[0];
                        try {
                            int score = Integer.parseInt(parts[1]);
                            // Keep highest score for each player
                            if (!scoresMap.containsKey(name) || score > scoresMap.get(name)) {
                                scoresMap.put(name, score);
                            }
                        } catch (NumberFormatException e) {
                            Gdx.app.log("MainMenu", "Failed to parse score: " + parts[1]);
                        }
                    }
                }
            }

            // Sort entries by score (highest first)
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(scoresMap.entrySet());
            sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            // Display top scores
            int count = Math.min(sortedEntries.size(), recordLabels.length);
            for (int i = 0; i < count; i++) {
                Map.Entry<String, Integer> entry = sortedEntries.get(i);
                recordLabels[i].setText((i + 1) + ". " + entry.getKey() + " - " + entry.getValue() + " pts");
            }

            // Clear remaining labels
            for (int i = count; i < recordLabels.length; i++) {
                recordLabels[i].setText((i + 1) + ". ---");
            }
        }
    }

    private void setupRecordsMenu() {
        Label recordsTitle = new Label("TOP 5 RECORDS", skin);

        recordLabels = new Label[5];
        for (int i = 0; i < 5; i++) {
            recordLabels[i] = new Label((i + 1) + ". Player " + (i + 1) + " - " + (10000 - i * 1000) + " pts", skin);
        }
        loadHighScores();

        recordsBackButton = new TextButton("ATRAS", skin);
        recordsBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                recordsTable.setVisible(false);
                mainTable.setVisible(true);
            }
        });

        recordsTable.add(recordsTitle).padBottom(20f).row();
        for (Label label : recordLabels) {
            recordsTable.add(label).padBottom(10f).row();
        }
        recordsTable.add(recordsBackButton).width(200).height(50).padTop(20f).row();
        recordsTable.center();
    }

    private void setupLevelSelectMenu() {
        Label levelSelectTitle = new Label("SELECCIONAR NIVEL", skin);

        levelButtons = new TextButton[4];
        String[] levelNames = {"NIVEL 1", "NIVEL 2", "NIVEL 3", "NIVEL 4"};

        for (int i = 0; i < 4; i++) {
            final int levelIndex = i;
            levelButtons[i] = new TextButton(levelNames[i], skin);
            levelButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("MainMenu", "Selected level " + (levelIndex + 1));
                    menuMusic.stop();

                    // captura de variable final para usar en el listener
                    switch (levelIndex) {
                        case 0:
                            game.setScreen(new PrimerNivel(game));
                            break;
                        case 1:
                            game.setScreen(new SegundoNivel(game));
                            break;
                        case 2:
                            game.setScreen(new TercerNivel(game));
                            break;
                        case 3:
                            //placeholder para nivel 4
                            game.setScreen(new PrimerNivel(game));
                            break;
                    }
                    dispose();
                }
            });
        }

        levelSelectBackButton = new TextButton("ATRAS", skin);
        levelSelectBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelSelectTable.setVisible(false);
                optionsTable.setVisible(true);
            }
        });

        levelSelectTable.add(levelSelectTitle).padBottom(20f).row();
        for (TextButton button : levelButtons) {
            levelSelectTable.add(button).width(200).height(50).padBottom(10f).row();
        }
        levelSelectTable.add(levelSelectBackButton).width(200).height(50).padTop(10f).row();
        levelSelectTable.center();
    }

    private void setupCreditsMenu() {
        Label creditsTitle = new Label("CREDITOS", skin);

        creditLabels = new Label[3];
        creditLabels[0] = new Label("Desarrollado por: Mi Nombre", skin);
        creditLabels[1] = new Label("Assets: Mi Nombre", skin);
        creditLabels[2] = new Label("Musica: Mi Nombre", skin);

        creditsBackButton = new TextButton("ATRAS", skin);
        creditsBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                creditsTable.setVisible(false);
                optionsTable.setVisible(true);
            }
        });

        creditsTable.add(creditsTitle).padBottom(20f).row();
        for (Label label : creditLabels) {
            creditsTable.add(label).padBottom(10f).row();
        }
        creditsTable.add(creditsBackButton).width(200).height(50).padTop(20f).row();
        creditsTable.center();
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
        menuMusic.dispose();
    }
}
