package com.minekart.sprites.interactive_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.Kart;

//TODO implementar InteractiveTileObject para poder darles posicion en el tiled
public class Coin extends InteractiveObject {
    public Coin(World world, Vector2 pos, Nivel screen, Texture texture){
        super(world,pos,screen, texture);
        this.setBounds(0, 0, 16 / MineKart.PPM, 16 / MineKart.PPM);
        this.setPosition(pos.x - (this.getWidth() / 2), pos.y - (this.getHeight() / 2));
    }

    @Override
    public void touched(Kart player){
        contacto = true;
//        player.sumaPuntuacion(MineKart.COIN_VALUE);
    }
}
