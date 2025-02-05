package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
import com.minekart.scenes.Hud;
import com.minekart.sprites.Coin;
import com.minekart.sprites.Eyeball;
import com.minekart.sprites.Kart;
import com.minekart.tools.B2WorldCreator;
import com.minekart.tools.WorldContactListener;

import java.util.ArrayList;

public class PlayScreen implements Screen {
    // main
    private MineKart game;

    //Camara etc
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    //tiled
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    // jugador
    private Kart kartPlayer;

    // boss
    private TextureAtlas eye_atlas;
    private Texture hand_texture;
    private Eyeball eyeball;

    //monedas
    public Texture coinTexture;
    public Array<Coin> arrayMonedas;

    public PlayScreen(MineKart game) {
        this.game = game;
        //recursos
        eye_atlas = new TextureAtlas("eye/eye2.atlas");
        hand_texture = new Texture("eye/mano_ruido.png");
        coinTexture = new Texture("coin.png");

        //camara
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MineKart.V_WIDTH / MineKart.PPM, MineKart.V_HEIGHT / MineKart.PPM, gameCam);
        hud = new Hud(game.batch);

        //mapa
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("test2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MineKart.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // box2d
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map);
        world.setContactListener(new WorldContactListener());

        //jugador
        kartPlayer = new Kart(world, this);
        eyeball = new Eyeball(world, this);
        eyeball.setPosition(0, -10);

        // creacion dinámica
        crearObjetosMundo();
    }

    // funcion cargar monedas
    public void crearObjetosMundo() {
        arrayMonedas = new Array<>();
        Vector2 posicion;
        int incremento = 50;
        int x2 = 512;
        float x = MathUtils.random(gamePort.getWorldWidth() / MineKart.PPM);
        float y = MathUtils.random(MineKart.V_HEIGHT / MineKart.PPM);
        for (int i = 0; i < 30; i++) {
            x2 += incremento;
            posicion = new Vector2(x2 / MineKart.PPM, 256 / MineKart.PPM);
            arrayMonedas.add(new Coin(world, posicion, this));
        }
    }

    //getters
    public TextureAtlas getAtlas() {
        return eye_atlas;
    }

    public Texture getHand_texture() {
        return hand_texture;
    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        kartPlayer.kartInput(dt);
        world.step(1 / 60f, 6, 2);
        kartPlayer.update(dt);
        eyeball.update(dt);
        hud.update(kartPlayer);
        gameCam.position.x = kartPlayer.b2Body.getPosition().x;
//        gameCam.position.y = kartPlayer.b2Body.getPosition().y; TODO posible mecánica
        gameCam.update();
        mapRenderer.setView(gameCam);

        //FIXME extraer un un metodo
        for (Coin c :
            arrayMonedas) {
            if (c != null && c.recogida && !world.isLocked()) {
                arrayMonedas.removeValue(c, true);
                world.destroyBody(c.body);
                c.body = null;
                c = null;
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        kartPlayer.draw(game.batch);
        eyeball.draw(game.batch);

        //FIXME extraer
        for (Coin c :
            arrayMonedas) {
            if (c != null) {
                c.draw(game.batch);
            }
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
