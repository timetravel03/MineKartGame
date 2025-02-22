package com.minekart.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
import com.minekart.sprites.Kart;


public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    public Label coinsLabel;
    public Label scoreLabel;
    public Label vidasLabel;
    public Label progresoLabel;
    public Label frutasLabel;

    public Hud(SpriteBatch sb) {
        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        vidasLabel = new Label(String.format("Vidas: %d", 4), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        progresoLabel = new Label(String.format("Progreso: %d", 0), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        frutasLabel = new Label(String.format("Frutas: x%d", 0), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        coinsLabel = new Label(String.format("Monedas: x%d", 0), new Label.LabelStyle(new BitmapFont(),Color.WHITE));

        table.add(vidasLabel).expandX().padTop(5);
        table.add(progresoLabel).expandX().padTop(5);
        table.row();
        table.add(frutasLabel);
        table.row();
        table.add(coinsLabel);

        stage.addActor(table);
    }

    public void update(Kart player){
        coinsLabel.setText(String.format("Monedas: x%d", player.getCantidad_monedas()));
        vidasLabel.setText(String.format("Vidas: %d", player.getCantidad_vidas()));
        frutasLabel.setText(String.format("Frutas: x%d", player.getFrutas()));
        progresoLabel.setText(String.format("X: %.0f Y: %.0f", player.b2Body.getPosition().x,  player.b2Body.getPosition().y));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
