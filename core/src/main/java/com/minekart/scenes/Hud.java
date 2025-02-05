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

    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    Label countDownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label kartLabel;
    Label version;

    String state = "";

    public Hud(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(MineKart.V_WIDTH, MineKart.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("MONO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label(state, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        kartLabel = new Label("PUNTOS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        version = new Label("v0.1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(kartLabel).expandX().padTop(5);
        table.add(worldLabel).expandX().padTop(5);
        table.add(timeLabel).expandX().padTop(5);
        table.row();
        table.add(scoreLabel).expandX();
//        table.add(levelLabel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);
    }

    public void update(Kart player){
        worldLabel.setText(player.getState().toString());
        scoreLabel.setText(player.getPuntuacion());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
