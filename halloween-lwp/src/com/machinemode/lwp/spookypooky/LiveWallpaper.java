package com.machinemode.lwp.spookypooky;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.machinemode.lwp.spookypooky.background.BackgroundMesh;
import com.machinemode.lwp.spookypooky.physics.SimpleWorld;
import com.machinemode.lwp.spookypooky.sprites.GameObjectPoolManager;
import com.machinemode.lwp.spookypooky.sprites.Ghost;

public class LiveWallpaper implements ApplicationListener
{
    World world;
    float worldWidth;
    private static final float WORLD_HEIGHT = 10.0f;
    private static final float BACKGROUND_SIZE = WORLD_HEIGHT * 3.0f;
    private float boundaryOffset = -5;
    private float boundaryPadding = 10;
    
    private Vector2 gravity = new Vector2(0.0f, 0.1f);
    private static final float TIME_STEP = 1.0f / 30.0f;
    private static final int VELOCITY_ITERATIONS = 10;
    private static final int POSITION_ITERATIONS = 10;
    
    static Random rand = new Random();

    private static WallpaperCamera camera = new WallpaperCamera();
    private BackgroundMesh backgroundMesh;
    private GameObjectPoolManager<Ghost> ghostManager;
    private SpriteBatch batch;
    
    @Override
    public void create()
    {
        Assets.load();
        float pixelWidth = Gdx.graphics.getWidth();
        float pixelHeight = Gdx.graphics.getHeight();

        if(pixelWidth < pixelHeight)
        {
            worldWidth = pixelWidth * (WORLD_HEIGHT / pixelHeight) * 2;
        }
        else
        {
            worldWidth = pixelHeight * (WORLD_HEIGHT / pixelWidth) * 2;
        }

        world = SimpleWorld.newWorld(gravity);
        backgroundMesh = new BackgroundMesh(BACKGROUND_SIZE,
                                            BACKGROUND_SIZE,
                                            worldWidth * 0.5f,
                                            WORLD_HEIGHT * 0.5f);

        batch = new SpriteBatch();

        Rectangle spriteBoundary = new Rectangle(boundaryOffset, boundaryOffset, worldWidth
                + boundaryPadding, WORLD_HEIGHT + boundaryPadding);

        ghostManager = new GameObjectPoolManager<Ghost>(50, 2, spriteBoundary)
        {
            @Override
            protected Ghost newGameObject()
            {
                float size = 1.0f + rand.nextFloat() * 0.5f;
                return new Ghost.Builder(Assets.ghost, size).angularDamping(0.2f)
                                                         .linearDamping(0.5f)
                                                         .density(0.3f)
                                                         .friction(0.4f)
                                                         .rotate(rand.nextBoolean())
                                                         .build(world);
            }

            @Override
            protected Vector2 getPosition()
            {
                return new Vector2(rand.nextInt((int)worldWidth), -2.0f);
            }

            @Override
            protected float getAngle()
            {
                return -0.5f + rand.nextFloat() * 1.0f;
            }
        };
    }

    @Override
    public void resume()
    {
        // TODO
    }

    @Override
    public void dispose()
    {
        // TODO
    }

    @Override
    public void render()
    {
        camera.update();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        backgroundMesh.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        ghostManager.update(batch);
        batch.end();

        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    @Override
    public void resize(int width, int height)
    {
        float viewportWidth;
        float viewportHeight;
        float aspectRatio;

        if(width < height)
        {
            viewportWidth = width * (WORLD_HEIGHT / height);
            viewportHeight = WORLD_HEIGHT;
        }
        else
        {
            viewportWidth = height * (WORLD_HEIGHT / width);
        }

        aspectRatio = (float)width / (float)height;
        viewportHeight = (width < height) ? WORLD_HEIGHT : viewportWidth;
        camera.resize(viewportWidth, viewportHeight, aspectRatio);
        backgroundMesh.updateVertices(new Vector2(worldWidth * 0.5f, viewportHeight * 0.5f));
    }

    @Override
    public void pause()
    {
        // TODO
    }

    public static void setOffset(float x, float y)
    {
        camera.setOffset(x, y);
    }
}
