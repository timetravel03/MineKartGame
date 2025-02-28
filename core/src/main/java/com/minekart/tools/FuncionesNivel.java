package com.minekart.tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.minekart.screens.niveles.Nivel;
import com.minekart.sprites.interactive_objects.FallingRock;

public class FuncionesNivel {
    public static void crearRocas(OrthographicCamera gameCam, Array<FallingRock> rocas, Texture rock_texture, World world, Nivel nivel) {
        float cam_position_left = gameCam.position.x + 1; // desde un poco mas adelante del personaje
        float cam_position_right = (gameCam.position.x + gameCam.viewportWidth / 2) + 1;
        float random = MathUtils.random(cam_position_left, cam_position_right);

        FallingRock rock = new FallingRock(world, new Vector2(random, gameCam.position.y + gameCam.viewportHeight/2), nivel, rock_texture);
        rocas.add(rock);
    }
}
