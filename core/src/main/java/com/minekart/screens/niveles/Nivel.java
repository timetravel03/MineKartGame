package com.minekart.screens.niveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minekart.MineKart;
import com.minekart.scenes.Hud;
import com.minekart.screens.PantallaResultados;
import com.minekart.sprites.Kart;
import com.minekart.tools.B2WorldCreator;
import com.minekart.tools.CheckPoint;
import com.minekart.tools.WorldContactListener;

import java.util.Hashtable;

public abstract class Nivel implements Screen {
    // juego
    protected MineKart game;

    // mapa
    protected TmxMapLoader mapLoader;
    protected TiledMap map;
    protected OrthogonalTiledMapRenderer mapRenderer;

    // camara
    protected OrthographicCamera gameCam;
    protected Viewport gamePort;
    protected Hud hud;

    // box2d
    protected World world;
    protected Box2DDebugRenderer b2dr;
    protected B2WorldCreator worldCreator;

    // kart
    protected Kart kartPlayer;

    //deltaTimer
    protected float deltaTimer;

    // variables de nivel
    protected int id;
    public boolean completado;
    public Array<Body> listaCuerposEliminar;
    protected Hashtable<String, Texture> textureHashtable;

    //TODO imlpementar puntos de respawn
    public Array<CheckPoint> respawnPoints;
    protected Vector2 ultRespawn;

    // cargar assets -> cargar colisiones -> renderizar extra
    public Nivel(MineKart game){
        // main
        this.game = game;

        // variables de nivel
        completado = false;
        listaCuerposEliminar = new Array<Body>();
        deltaTimer = 0;
        respawnPoints = new Array<>();

        // camara
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MineKart.V_WIDTH / MineKart.PPM, MineKart.V_HEIGHT / MineKart.PPM, gameCam);
        hud = new Hud(game.batch);

        // mapa
        textureHashtable = new Hashtable<>();
        mapLoader = new TmxMapLoader();
        cargarAssets(); // assets (tiene que estar en medio, porque necesita el maploader y renderer necesita el map asiq ajajajaj)
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/MineKart.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


        // box2d
        world = new World(new Vector2(0, -7.5f), true);
        b2dr = new Box2DDebugRenderer();
        worldCreator = new B2WorldCreator(world, map, this);
        cargarColisiones();
        world.setContactListener(new WorldContactListener());

        // kart
        kartPlayer = new Kart(world);
    }

    // carga assets y el mapa
    abstract void cargarAssets();

    abstract void cargarColisiones();

    // renderizar elementos específicos del nivel
    abstract void renderizarExtra(SpriteBatch batch);

    protected void update(float delta){
        // actualizar mundo
        world.step(1/60f, 6, 2); // (se podria investigar que significan los parametros)

        // actualizar jugador y procesar inputs
        kartPlayer.update(delta);
        hud.update(kartPlayer);
        gameCam.position.x = kartPlayer.b2Body.getPosition().x;

        // eliminar cuerpos
        for (Body body : listaCuerposEliminar) {
            world.destroyBody(body);
        }
        listaCuerposEliminar.clear();

        // actualizar camara
        gameCam.update();
        mapRenderer.setView(gameCam);

        // TODO extraer a un metodo y hacerlo dinámico (osea que haya diferentes puntos de respawn)?
        if (kartPlayer.reaparecer) {
            kartPlayer.reaparecer = false;
            kartPlayer.b2Body.setTransform(kartPlayer.ultimoCheckPoint.puntoMasBajo(), 0);
        }
    }

    public void removeBody(Body body) {
        listaCuerposEliminar.add(body);
    }

    public int getID() {
        return id;
    }

    public OrthographicCamera getCamera() {
        return gameCam;
    }

    @Override
    public void show() {

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
        renderizarExtra(game.batch);
        game.batch.end();

        // comprueba si se ha completado el nivel, no puede ir en update porque se ejecuta antes de que se renderice el mundo y causaria problemas
        if (completado){
            this.dispose();
            game.setScreen(new PantallaResultados(game, kartPlayer.getCantidad_monedas(), kartPlayer.getFrutas(), kartPlayer.getCantidad_vidas()));
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height); //MUY MUY MUY MUY MUY MUY MUY MUY IMPORTANTE AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
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
