package com.minekart.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
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

    public Hud(SpriteBatch sb) {
        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        vidasLabel = new Label(String.format("Vidas: %d", 4), skin);
//        progresoLabel = new Label(String.format("Progreso: %d", 0), skin);
        frutasLabel = new Label(String.format("Cajetillas: x%d", 0), skin);
        coinsLabel = new Label(String.format("Monedas: x%d", 0), skin);

        //FIXME arreglar el bton de pause
        pauseButton = new TextButton("II", skin);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause = !pause;
            }
        });

        table.add(vidasLabel).expandX().padTop(5);
//        table.add(progresoLabel).expandX().padTop(5);
        table.row();
        table.add(frutasLabel);
        table.row();
        table.add(coinsLabel);
        table.add(pauseButton).expandX().padTop(5);

        stage.addActor(table);
    }

    public void update(Kart player){
        coinsLabel.setText(String.format("Monedas: x%d", player.getCantidad_monedas()));
        vidasLabel.setText(String.format("Vidas: %d", player.getCantidad_vidas()));
        frutasLabel.setText(String.format("Frutas: x%d", player.getFrutas()));
//        progresoLabel.setText(String.format("X: %.0f Y: %.0f", player.b2Body.getPosition().x,  player.b2Body.getPosition().y));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
