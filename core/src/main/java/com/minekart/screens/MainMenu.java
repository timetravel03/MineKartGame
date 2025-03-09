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
import java.util.List;
import java.util.Locale;
import java.util.Map;

// clase del menu principal
public class MainMenu extends ScreenAdapter {
    private Stage stage;
    private Table mainTable;
    private Table optionsTable;
    private Table recordsTable;
    private Table creditsTable;
    private Table levelSelectTable;
    private Viewport viewport;
    private Label titulo;
    private Table helpTable;

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
    private TextButton helpButton;

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

    // idioma


    public MainMenu(MineKart game) {
        // variables varias
        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        this.game = game;
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // musica de fondo
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/drone.ogg"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(MineKart.volume);
        menuMusic.play();

        // creacion de las tablas
        mainTable = new Table();
        optionsTable = new Table();
        recordsTable = new Table();
        creditsTable = new Table();
        levelSelectTable = new Table();
        helpTable = new Table();

        // tablas rellenan la pantalla
        mainTable.setFillParent(true);
        optionsTable.setFillParent(true);
        recordsTable.setFillParent(true);
        creditsTable.setFillParent(true);
        levelSelectTable.setFillParent(true);
        helpTable.setFillParent(true);

        // ocultar todas las tablas excepto la principal
        optionsTable.setVisible(false);
        recordsTable.setVisible(false);
        creditsTable.setVisible(false);
        levelSelectTable.setVisible(false);
        helpTable.setVisible(false);

        // creacion de los elementos de los menus
        setupMainMenu();
        setupOptionsMenu();
        setupRecordsMenu();
        setupLevelSelectMenu();
        setupCreditsMenu();
        setupHelpMenu();

        // añadir las tablas a la pantalla
        stage.addActor(mainTable);
        stage.addActor(optionsTable);
        stage.addActor(recordsTable);
        stage.addActor(creditsTable);
        stage.addActor(levelSelectTable);
        stage.addActor(helpTable);
    }

    // menu principal
    private void setupMainMenu() {
        titulo = new Label(MineKart.myBundle.get("title"), skin);

        jugarButton = new TextButton(MineKart.myBundle.get("play"), skin);
        jugarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Jugar");
                menuMusic.stop();
                game.setScreen(new PrimerNivel(game));
                dispose();
                game.puntacionActual = 0;
            }
        });

        opcionesButton = new TextButton(MineKart.myBundle.get("options"), skin);
        opcionesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Opciones");
                mainTable.setVisible(false);
                optionsTable.setVisible(true);
            }
        });

        recordsButton = new TextButton(MineKart.myBundle.get("records"), skin);
        recordsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenu", "Records");
                mainTable.setVisible(false);
                recordsTable.setVisible(true);
            }
        });

        salirButton = new TextButton(MineKart.myBundle.get("exit"), skin);
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

    // menu de opciones
    private void setupOptionsMenu() {
        Label optionsTitle = new Label(MineKart.myBundle.get("options"), skin);

        // TODO string format
        volumeLabel = new Label(String.format("%s: %.0f%%", MineKart.myBundle.get("volume"), MineKart.volume * 100), skin);
        sliderVolumen = new Slider(0, 100, 1, false, skin);
        sliderVolumen.setValue(MineKart.volume * 100);
        sliderVolumen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                volumeLabel.setText(MineKart.myBundle.get("volume") + ": " + (int) sliderVolumen.getValue() + "%");
                MineKart.volume = sliderVolumen.getValue() / 100f;
                menuMusic.setVolume(MineKart.volume);
                MineKart.fxVolume = MineKart.volume * 0.2f;
            }
        });

        vibracionButton = new TextButton(MineKart.vibracion ? MineKart.myBundle.get("vibrate") + ": ON" : MineKart.myBundle.get("vibrate") + ": OFF", skin);
        vibracionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MineKart.vibracion = !MineKart.vibracion;
                vibracionButton.setText(MineKart.vibracion ? MineKart.myBundle.get("vibrate") + ": ON" : MineKart.myBundle.get("vibrate") + ": OFF");
            }
        });

        levelSelectButton = new TextButton(MineKart.myBundle.get("courses"), skin);
        levelSelectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsTable.setVisible(false);
                levelSelectTable.setVisible(true);
            }
        });

        String currentLanguage = MineKart.locale == null ? "ES" : "EN";
        idiomaButton = new TextButton(MineKart.myBundle.get("language") + ": " + currentLanguage, skin);
        idiomaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String currentText = idiomaButton.getText().toString();
                if (currentText.contains("ES")) {
                    idiomaButton.setText(MineKart.myBundle.get("language") + ": EN");
                    MineKart.locale = new Locale("en");
                } else {
                    idiomaButton.setText(MineKart.myBundle.get("language") + ": ES");
                    MineKart.locale = null;
                }
                MineKart.inicializarBundle();
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });

        creditosButton = new TextButton(MineKart.myBundle.get("credits"), skin);
        creditosButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsTable.setVisible(false);
                creditsTable.setVisible(true);
            }
        });

        atrasButton = new TextButton(MineKart.myBundle.get("back"), skin);
        atrasButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsTable.setVisible(false);
                mainTable.setVisible(true);
            }
        });

        helpButton = new TextButton(MineKart.myBundle.get("help"), skin);
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsTable.setVisible(false);
                helpTable.setVisible(true);
            }
        });

        optionsTable.add(optionsTitle).padBottom(20f).row();
        optionsTable.add(volumeLabel).padBottom(5f).row();
        optionsTable.add(sliderVolumen).width(200).padBottom(10f).row();
        optionsTable.add(vibracionButton).width(200).height(40).padBottom(10f).row();
        optionsTable.add(levelSelectButton).width(200).height(40).padBottom(10f).row();
        optionsTable.add(idiomaButton).width(200).height(40).padBottom(10f).row();
        optionsTable.add(helpButton).width(200).height(40).padBottom(10f).row();
        optionsTable.add(creditosButton).width(200).height(40).padBottom(10f).row();
        optionsTable.add(atrasButton).width(200).height(40).row();
        optionsTable.center();
    }

    // TODO REVISAR carga los records de las preferencias, ordenados por puntuacion
    private void loadHighScores() {
        Preferences prefs = Gdx.app.getPreferences("minekart-preferences");
        String highScores = prefs.getString("highscores", "");

        if (!highScores.isEmpty()) {
            String[] entries = highScores.split(",");

            Map<String, Integer> scoresMap = new HashMap<>();

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

            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(scoresMap.entrySet());
            sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            int count = Math.min(sortedEntries.size(), recordLabels.length);
            for (int i = 0; i < count; i++) {
                Map.Entry<String, Integer> entry = sortedEntries.get(i);
                recordLabels[i].setText((i + 1) + ". " + entry.getKey() + " - " + entry.getValue() + " pts");
            }

            for (int i = count; i < recordLabels.length; i++) {
                recordLabels[i].setText((i + 1) + ". ---");
            }
        }
    }

    // pantalla de records
    private void setupRecordsMenu() {
        Label recordsTitle = new Label("TOP 5 RECORDS", skin);

        recordLabels = new Label[5];
        for (int i = 0; i < 5; i++) {
            recordLabels[i] = new Label((i + 1) + ". Player " + (i + 1) + " - " + (10000 - i * 1000) + " pts", skin);
        }
        loadHighScores();

        recordsBackButton = new TextButton(MineKart.myBundle.get("back"), skin);
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

    // pantalla de seleccion de nivel
    private void setupLevelSelectMenu() {
        Label levelSelectTitle = new Label(MineKart.myBundle.get("selectCourse"), skin);

        levelButtons = new TextButton[3];
        String[] levelNames = {MineKart.myBundle.get("course") + " 1", MineKart.myBundle.get("course") + " 2", MineKart.myBundle.get("course") + " 3"};

        for (int i = 0; i < 3; i++) {
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
                    }
                    dispose();
                }
            });
        }

        levelSelectBackButton = new TextButton(MineKart.myBundle.get("back"), skin);
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

    // pantalla de creditos
    private void setupCreditsMenu() {
        Label creditsTitle = new Label(MineKart.myBundle.get("credits"), skin);

        creditLabels = new Label[8];
        creditLabels[0] = new Label(MineKart.myBundle.get("development") + ": Telmo Iglesias", skin);
        creditLabels[1] = new Label("Pixel Fantasy Caves: SZADI ART", skin);
        creditLabels[2] = new Label("A Bunch Of Rocks: verzatiledev", skin);
        creditLabels[3] = new Label("UI Skins: czyzby, yuripourre, crykn", skin);
        creditLabels[4] = new Label(MineKart.myBundle.get("musicAndSounds") + ": Telmo/Amanda", skin);
        creditLabels[5] = new Label("Box2d: Eric Catto", skin);
        creditLabels[6] = new Label("LibGDX: BadLogicGames", skin);
        creditLabels[7] = new Label("Extras: Telmo Iglesias", skin);

        creditsBackButton = new TextButton(MineKart.myBundle.get("back"), skin);
        creditsBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                creditsTable.setVisible(false);
                optionsTable.setVisible(true);
            }
        });

        creditsTable.add(creditsTitle).padBottom(10f).row();
        for (Label label : creditLabels) {
            creditsTable.add(label).padBottom(10f).row();
        }
        creditsTable.add(creditsBackButton).width(200).height(50).padTop(20f).row();
        creditsTable.center();
    }

    private void setupHelpMenu() {
        Label helpTitle = new Label(MineKart.myBundle.get("help"), skin);

        // Create multi-line text with game instructions
        Label helpText = new Label(MineKart.myBundle.get("helpText"), skin);
        helpText.setWrap(true);

        TextButton helpBackButton = new TextButton(MineKart.myBundle.get("back"), skin);
        helpBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                helpTable.setVisible(false);
                optionsTable.setVisible(true);
            }
        });

        helpTable.add(helpTitle).padBottom(20f).row();
        helpTable.add(helpText).width(400).padBottom(20f).row();
        helpTable.add(helpBackButton).width(200).height(50).row();
        helpTable.center();
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
