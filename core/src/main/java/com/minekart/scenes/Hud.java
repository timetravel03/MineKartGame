package com.minekart.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.Kart;


public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    public Label coinsLabel;
    public Label vidasLabel;
    public Label progresoLabel;
    public Label frutasLabel;
    private Skin skin;
    private TextButton pauseButton;
    public boolean pause;
    private Nivel nivel;
    private Label pauseLabel;


    public Hud(SpriteBatch sb, Nivel nivel) {
        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        this.nivel = nivel;

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        vidasLabel = new Label(String.format("%s: %d", MineKart.myBundle.get("lives"), 4), skin);
        progresoLabel = new Label(String.format("Progreso: %d", 0), skin);
        frutasLabel = new Label(String.format("%s: %d", MineKart.myBundle.get("packs"), 0), skin);
        coinsLabel = new Label(String.format("%s: %d", MineKart.myBundle.get("coins"), 0), skin);
        pauseLabel = new Label(MineKart.myBundle.get("pause"), skin);
        pauseLabel.setVisible(false);

        //FIXME arreglar el bton de pause
        pauseButton = new TextButton("II", skin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Hud", "Pause");
                MineKart.sounds.get("pause").play(MineKart.volume);
                pause = !pause;
                pauseLabel.setVisible(pause);
            }
        });

        table.add(vidasLabel).expandX().padTop(5f);
        table.add(pauseButton).expandX().padTop(10f);
        table.row();
        table.add(frutasLabel).padTop(5f);
        table.row();
        table.add(coinsLabel).padTop(10f);

        // tabla solo para el label de pause, que este centrado
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();
        pauseTable.add(pauseLabel);

        stage.addActor(table);
        stage.addActor(pauseTable);
    }

    // este metodo solo exite para evitar que el jugador salte cuando se toca algun boton del hud, no se si existe alguna mejor forma de hacerlo
    // lo dejo aqui pero podria estar en la clase Nivel
    private void kartInputProcessing(Kart player) {
        Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        stage.screenToStageCoordinates(touchPoint);

        if (stage.hit(touchPoint.x, touchPoint.y, true) == null) {
            player.kartInput(Gdx.input.justTouched(), Gdx.input.isTouched());
        }
    }

    public void update(Kart player) {
        kartInputProcessing(player);
        coinsLabel.setText(String.format("%s: %d", MineKart.myBundle.get("coins"), player.getCantidad_monedas()));
        if (player.getCantidad_vidas() >= 0)
            vidasLabel.setText(String.format("%s: %d", MineKart.myBundle.get("lives"), player.getCantidad_vidas()));
        frutasLabel.setText(String.format("%s: %d", MineKart.myBundle.get("packs"), player.getFrutas()));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
