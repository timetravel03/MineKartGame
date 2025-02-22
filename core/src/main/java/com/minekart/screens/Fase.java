package com.minekart.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;

// Esta interfaz deben implementarla todas las fases del juego, asi se pueden identificar en otras partes del código, a pesar de ser distintas
public interface Fase {
    void removeBody(Body body);
    int getID();
    OrthographicCamera getCamera();
}
