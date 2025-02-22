package com.minekart.sprites.interactive_objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.Fase;
import com.minekart.screens.Nivel;
import com.minekart.sprites.Kart;

public class FallingRock extends InteractiveObject {
    private OrthographicCamera cam;
    private float fallingSpeed;
    public boolean eliminar;

    public FallingRock(World world, Vector2 pos, Nivel screen, Texture texture) {
        super(world, pos, screen, texture);
        cam = screen.getCamera();
        fallingSpeed = -.7f;
        body.setLinearVelocity(new Vector2(0, fallingSpeed));
        eliminar = false;
        this.setSize(32/ MineKart.PPM,32/MineKart.PPM);
        setOrigin(getWidth()/2, getHeight()/2);

    }

    @Override
    public void touched(Kart player) {
        screen.removeBody(body);
        player.setCantidad_vidas(player.getCantidad_vidas() - 1);
        eliminar = true;
        player.reaparecer = true;
    }

    public void update(float dt) {
        //mover sprite
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        if (cam.viewportHeight > body.getPosition().y){
//            eliminar = true;
//            screen.removeBody(body);
        }
        this.rotate(1f+dt);
    }
}
