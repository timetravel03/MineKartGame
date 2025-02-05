package com.minekart.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.minekart.MineKart;
import com.minekart.sprites.GroundObstacle;
import com.minekart.sprites.InteractiveTileObject;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map) {
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        // ground
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / MineKart.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / MineKart.PPM);

            body = world.createBody(bDef);
            shape.setAsBox((rectangle.getWidth() / 2) / MineKart.PPM, (rectangle.getHeight() / 2) / MineKart.PPM);
            fDef.shape = shape;
//            fDef.restitution = .9f;
            body.createFixture(fDef);
        }

//         obstacles
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            new GroundObstacle(world, map, rectangle);
        }

        //rebotes
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / MineKart.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / MineKart.PPM);

            body = world.createBody(bDef);
            shape.setAsBox((rectangle.getWidth() / 2) / MineKart.PPM, (rectangle.getHeight() / 2) / MineKart.PPM);
            fDef.shape = shape;
            fDef.restitution = .9f;
            body.createFixture(fDef);
        }


        //polygon
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(PolygonMapObject.class)) {
            Polygon polygon = ((PolygonMapObject) object).getPolygon();
            BodyDef bodyDef = new BodyDef();
            FixtureDef fixtureDef = new FixtureDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(polygon.getOriginX(), polygon.getOriginY());
            Body bodyPol = world.createBody(bodyDef);
            PolygonShape polygonShape = new PolygonShape();
            fixtureDef.shape = convertPolygonToPolygonShape(polygon, MineKart.PPM);
            fixtureDef.friction = 0f;
            bodyPol.createFixture(fixtureDef);
        }
    }

    // chatgpt (revisar y entender)
    public PolygonShape convertPolygonToPolygonShape(Polygon polygon, float pixelsPerMeter) {
        PolygonShape polygonShape = new PolygonShape();

        // Get the transformed vertices from the LibGDX Polygon
        float[] vertices = polygon.getTransformedVertices();

        // Convert the vertices to Box2D's format (scaled to meters)
        Vector2[] box2dVertices = new Vector2[vertices.length / 2];
        for (int i = 0; i < vertices.length / 2; i++) {
            float x = vertices[i * 2] / pixelsPerMeter;
            float y = vertices[i * 2 + 1] / pixelsPerMeter;
            box2dVertices[i] = new Vector2(x, y);
        }

        // Ensure the vertices form a convex polygon (Box2D requires this)
        if (box2dVertices.length <= 8) { // Box2D supports up to 8 vertices
            float[] convexVertices = new float[box2dVertices.length * 2];
            for (int i = 0; i < box2dVertices.length; i++) {
                convexVertices[i * 2] = box2dVertices[i].x;
                convexVertices[i * 2 + 1] = box2dVertices[i].y;
            }
            polygonShape.set(convexVertices);
        } else {
            throw new IllegalArgumentException("Polygon has too many vertices for Box2D (max 8).");
        }

        return polygonShape;
    }
}
