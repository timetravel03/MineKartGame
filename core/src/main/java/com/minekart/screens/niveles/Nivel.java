package com.minekart.screens.niveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.minekart.screens.MainMenu;
import com.minekart.screens.PantallaMuerte;
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
    public Kart kartPlayer;

    //deltaTimer
    protected float deltaTimer;

    // variables de nivel
    protected int id;
    public boolean completado;
    public Array<Body> listaCuerposEliminar;
    protected Hashtable<String, Texture> textureHashtable;

    // punto de respawn
    public Array<CheckPoint> respawnPoints;
    protected Vector2 ultRespawn;

    // alpha de la textura de fade in y fade out
    private float fadeOutAlpha = 0f;
    private float fadeInAlpha = 1f;

    // booleana para controlar el fade in
    private boolean fadeIn = true;

    // booleana para controlar el pause del juego
    private boolean paused = false;

    //referencia al siguiente nivel
    protected Nivel siguienteNivel;

    // indica si es el nivel final
    protected boolean finalLevel;

    // musica del nivel
    Music music;

    //orden: cargar assets -> cargar colisiones -> renderizar extra
    public Nivel(MineKart game) {
        // main
        this.game = game;

        // variables de nivel
        completado = false;
        listaCuerposEliminar = new Array<Body>();
        deltaTimer = 0;
        respawnPoints = new Array<>();
        finalLevel = false;

        // camara
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MineKart.V_WIDTH / MineKart.PPM, MineKart.V_HEIGHT / MineKart.PPM, gameCam);
        hud = new Hud(game.batch, this);

        // mapa
        textureHashtable = new Hashtable<>();
        mapLoader = new TmxMapLoader();
        cargarAssets(); // assets (tiene que estar en medio, porque necesita el maploader y renderer necesita el map asiq ajajajaj)
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MineKart.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // box2d
        world = new World(new Vector2(0, -7.5f), true);
        b2dr = new Box2DDebugRenderer();
        worldCreator = new B2WorldCreator(world, map, this);
        cargarColisiones();
        world.setContactListener(new WorldContactListener());

        // kart
        kartPlayer = new Kart(world);
        encontrarPrimerCheckpoint();
    }

    protected void encontrarPrimerCheckpoint() {
        CheckPoint temp = respawnPoints.get(0);
        for (CheckPoint c :
            respawnPoints) {
            if (c.getPosition().x < temp.getPosition().x) {
                temp = c;
            }
        }
        kartPlayer.setPosicionInicial(temp.puntoMasBajo());
    }

    // carga assets y el mapa
    abstract void cargarAssets();

    // carga las colisiones del nivel, se puede elegir cuales se quieren cargar usando las funciones del world creator
    abstract void cargarColisiones();

    // renderizar elementos específicos del nivel
    abstract void renderizarExtra(SpriteBatch batch);

    protected void update(float delta) {
        // actualizar mundo
        world.step(1 / 60f, 6, 2); // (se podria investigar que significan los parametros)

        // actualizar jugador, hud y camara(posicion) y procesar inputs
        kartPlayer.update(delta, Gdx.input.justTouched(), Gdx.input.isTouched());
        hud.update(kartPlayer);
        gameCam.position.x = kartPlayer.b2Body.getPosition().x;

        // eliminar cuerpos que se marcaron con la funcion removeBody
        for (Body body : listaCuerposEliminar) {
            world.destroyBody(body);
        }
        listaCuerposEliminar.clear();

        // actualizar camara
        gameCam.update();
        mapRenderer.setView(gameCam);

        // reaaparecer en ultimo checkpoint
        if (kartPlayer.reaparecer) {
            MineKart.sounds.get("hit").play(MineKart.fxVolume);
            kartPlayer.reaparecer = false;
            kartPlayer.b2Body.setTransform(kartPlayer.ultimoCheckPoint.puntoMasBajo(), 0);
        }

        if (completado) {
            music.stop();
        }

        if (kartPlayer.getCantidad_vidas() < 0) {
            music.stop();
        }
    }

    // pausar el juego
    public void togglePause() {
        paused = !paused;
    }

    // agregar cuerpo a la lista de cuerpos a eliminar
    public void removeBody(Body body) {
        listaCuerposEliminar.add(body);
    }

    // obtener id del nivel (la variable no esta inicializada, debe inicializarse en cada nivel)
    public int getID() {
        return id;
    }

    // devuelve la camara del nivel
    public OrthographicCamera getCamera() {
        return gameCam;
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.stage);
        multiplexer.addProcessor(Gdx.input.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {

        // si esta pausado no se actualiza el mundo, depende de una variable del hud
        paused = hud.pause;
        if (!paused) {
            update(delta);
        }

        // limipar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renderizar mapa
        mapRenderer.render();

        //debug renderer
//        b2dr.render(world, gameCam.combined);


        //renderizar el resto de cosas
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        kartPlayer.draw(game.batch);
        renderizarExtra(game.batch);
        game.batch.end();

        //renderizar hud
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //TODO por que no funciona el boton de pausa?
        hud.stage.act(delta);
        hud.stage.draw();

        //fade in
        if (fadeIn) {
            fadeIn(delta);
        }

        // comprueba si se ha completado el nivel, no puede ir en update porque se ejecuta antes de que se renderice el mundo y causaria problemas
        if (completado) {
            hud.pause = true;
            if (finalLevel) {
                fadeOutTo(new PantallaMuerte(game, kartPlayer.getPuntuacion(), finalLevel, completado), delta);
            } else {
                fadeOutTo(new PantallaResultados(game,
                    kartPlayer.getPuntuacion(),
                    kartPlayer.getFrutas(),
                    kartPlayer.getCantidad_monedas(),
                    kartPlayer.getCantidad_vidas(), siguienteNivel), delta);
            }
        }

        //cuando te quedas sin vidas te devuelve a la pantalla de titulo
        if (kartPlayer.getCantidad_vidas() < 0) {
            hud.pause = true;
            fadeOutTo(new PantallaMuerte(game, kartPlayer.getPuntuacion(), finalLevel, completado), delta);
        }
    }

    // hace fade in de negro, para inicio del nivel
    protected void fadeIn(float delta) {
        fadeInAlpha -= delta * 0.75f;
        if (fadeInAlpha <= 0f) {
            fadeInAlpha = 0f;
            fadeIn = false;
        }
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.setColor(0, 0, 0, fadeInAlpha);
        game.batch.draw(new Texture("black.png"),
            gameCam.position.x - gamePort.getWorldWidth() / 2,
            gameCam.position.y - gamePort.getWorldHeight() / 2,
            gamePort.getWorldWidth(), gamePort.getWorldHeight());
        game.batch.setColor(1, 1, 1, 1);
        game.batch.end();
    }

    // hace fade out a otra pantalla
    protected void fadeOutTo(Screen screen, float delta) {
        fadeOutAlpha += delta * 0.75f;
        if (fadeOutAlpha >= 1f) {
            if (screen instanceof MainMenu) { //si se muere hace dispose de todos los recursos
                this.dispose();
            }
            fadeOutAlpha = 1f;
            game.setScreen(screen);
        }

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.setColor(0, 0, 0, fadeOutAlpha);
        game.batch.draw(new Texture("black.png"),
            gameCam.position.x - gamePort.getWorldWidth() / 2,
            gameCam.position.y - gamePort.getWorldHeight() / 2,
            gamePort.getWorldWidth(), gamePort.getWorldHeight());
        game.batch.setColor(1, 1, 1, 1);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height); //MUY IMPORTANTE
        hud.resize(width, height);
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
        music.stop();
        music.dispose();
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
