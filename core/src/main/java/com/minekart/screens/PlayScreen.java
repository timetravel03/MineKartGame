package com.minekart.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
import com.minekart.scenes.Hud;
import com.minekart.sprites.Eyeball;
import com.minekart.sprites.Kart;
import com.minekart.sprites.interactive_objects.FallingRock;
import com.minekart.tools.B2WorldCreator;
import com.minekart.tools.WorldContactListener;

public class PlayScreen implements Screen, Fase {
    //pantalla completado
    public boolean completada;

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

    //deltaTimer
    private float deltaTimer;

    //    //monedas
//    public Texture coinTexture;
//    public Array<Coin> arrayMonedas;
//
//    //frutas
    private Texture rock_texture;
    public Array<FallingRock> rocas;
//    public Array<Fruit> arrayBananas;

    public static Array<Body> listaCuerposEliminar;
    public final int ID = 0;

    public PlayScreen(MineKart game) {
        this.game = game;
        completada = false;
        listaCuerposEliminar = new Array<>();
        rocas = new Array<>();
        deltaTimer = 0;
        //recursos
        eye_atlas = new TextureAtlas("eye/eye2.atlas");
        hand_texture = new Texture("eye/mano_ruido.png");
        rock_texture = new Texture("rocas/rock3.png");

        //camara
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MineKart.V_WIDTH / MineKart.PPM, MineKart.V_HEIGHT / MineKart.PPM, gameCam);
        hud = new Hud(game.batch);

        //mapa
        mapLoader = new TmxMapLoader();
//        map = mapLoader.load("mapa_1.tmx");
        map = mapLoader.load("test2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MineKart.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // box2d
        world = new World(new Vector2(0, -7.5f), true);
        b2dr = new Box2DDebugRenderer();
//        B2WorldCreator worldCreator = new B2WorldCreator(world, map, this);
//        worldCreator.crearSuelo();
//        worldCreator.crearMonedas();
//        worldCreator.crearTodo();
        world.setContactListener(new WorldContactListener());

        //jugador
        kartPlayer = new Kart(world);
//        eyeball = new Eyeball(world, this);
//        eyeball.setPosition(0, -10);
    }

    private void crearRocas() {
        float cam_position_left = gameCam.position.x + 1; // desde un poco mas adelante del personaje
        float cam_position_right = (gameCam.position.x + gameCam.viewportWidth / 2) + 1;
        float random = MathUtils.random(cam_position_left, cam_position_right);

//        FallingRock rock = new FallingRock(world, new Vector2(random, gameCam.position.y + gameCam.viewportHeight/2), this, rock_texture);
//        rocas.add(rock);
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
//        eyeball.update(dt, kartPlayer);
        hud.update(kartPlayer);
        gameCam.position.x = kartPlayer.b2Body.getPosition().x;
//        gameCam.position.y = kartPlayer.b2Body.getPosition().y; TODO posible mecánica
        gameCam.update();
        mapRenderer.setView(gameCam);

        for (int i = listaCuerposEliminar.size - 1; i >= 0; i--) {
            world.destroyBody(listaCuerposEliminar.get(i));
            listaCuerposEliminar.removeIndex(i);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            kartPlayer.b2Body.setTransform(28, 3, 0);
//            eyeball.eye_body.setTransform(kartPlayer.b2Body.getPosition().x + 2, kartPlayer.b2Body.getPosition().y, 0);
            //192,19
        }
        if (kartPlayer.reaparecer) {
            kartPlayer.reaparecer = false;
            kartPlayer.b2Body.setTransform(300 / MineKart.PPM, 270 / MineKart.PPM, 0);
//            eyeball.eye_body.setTransform(kartPlayer.b2Body.getPosition().x + 2, kartPlayer.b2Body.getPosition().y, 0);
        }

        deltaTimer += dt;
        if (deltaTimer > 5f) {
            deltaTimer = 0;
            crearRocas();
        }

        for (int i = rocas.size - 1; i >= 0; i--) {
            rocas.get(i).update(dt);
            if (rocas.get(i).eliminar){
                rocas.removeIndex(i);
            }
        }

        //FIXME extraer un un metodo
//        for (Coin c :
//            arrayMonedas) {
//            if (c != null && c.contacto && !world.isLocked()) {
//                arrayMonedas.removeValue(c, true);
//                world.destroyBody(c.body);
//                c.body = null;
//                c = null;
//            }
//        }

//        for (Fruit f :
//            arrayBananas) {
//            if (f != null && f.contacto && !world.isLocked()) {
//                arrayBananas.removeValue(f, true);
//                world.destroyBody(f.body);
//                f.body = null;
//                f = null;
//            }
//        }

//        if (kartPlayer.b2Body.getPosition().x > 1024/MineKart.PPM){
//            completado = true;
//            hud.worldLabel.setText("NIVEL COMPLETADO");
//        }
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
//        kartPlayer.draw(game.batch);
//        eyeball.draw(game.batch);

        for (FallingRock r:
             rocas) {
            r.draw(game.batch);
        }

        //FIXME extraer
//        for (Coin c :
//            arrayMonedas) {
//            if (c != null) {
//                c.draw(game.batch);
//            }
//        }
//
//        for (Fruit f :
//            arrayBananas) {
//            if (f != null){
//                f.draw(game.batch);
//            }
//        }

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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    @Override
    public void removeBody(Body body) {
        listaCuerposEliminar.add(body);
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public OrthographicCamera getCamera() {
        return gameCam;
    }
}
