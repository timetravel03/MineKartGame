package com.minekart.sprites.interactive_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.Kart;

public class Fruit extends InteractiveObject {
    public Fruit(World world, Vector2 pos, Nivel screen, Texture texture) {
        super(world, pos, screen, texture);
        this.setBounds(0, 0, 32 / MineKart.PPM, 32 / MineKart.PPM);
        this.setPosition(pos.x - (this.getWidth() / 2), pos.y - (this.getHeight() / 2));
    }

    @Override
    public void touched(Kart player) {
        contacto = true;
//        player.sumaPuntuacion(MineKart.FRUIT_VALUE);
    }
}
