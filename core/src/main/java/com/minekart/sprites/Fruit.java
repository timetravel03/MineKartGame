package com.minekart.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.screens.PlayScreen;

public class Fruit extends InteractiveObject {
    public Fruit(World world, Vector2 pos, PlayScreen screen, Texture texture) {
        super(world, pos, screen, texture);
        this.setBounds(0, 0, 32 / MineKart.PPM, 32 / MineKart.PPM);
        this.setPosition(pos.x - (this.getWidth() / 2), pos.y - (this.getHeight() / 2));
    }

    @Override
    public void touched(Kart player) {
        recogida = true;
//        player.sumaPuntuacion(MineKart.FRUIT_VALUE);
    }
}
