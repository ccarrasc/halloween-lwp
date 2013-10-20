package com.machinemode.lwp.halloween;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.machinemode.lwp.halloween.background.BackgroundMesh;
import com.machinemode.lwp.halloween.physics.SimpleWorld;
import com.machinemode.lwp.halloween.sprites.Ghost;
import com.machinemode.lwp.halloween.sprites.GameObjectPoolManager;

public class LiveWallpaper implements ApplicationListener
{
    protected World world;
    protected final float worldHeight = 10.0f;
    protected float worldWidth;
    private Vector2 gravity = new Vector2(0.0f, 0.1f);
    private static final float TIME_STEP = 1f / 45f;
    private float boundaryOffset = -5;
    private float boundaryPadding = 10;
    
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
            worldWidth = pixelWidth * (worldHeight / pixelHeight) * 2;
        }
        else
        {
            worldWidth = pixelHeight * (worldHeight / pixelWidth) * 2;
        }

        world = SimpleWorld.newWorld(gravity);
        backgroundMesh = new BackgroundMesh(30f, 30f, worldWidth * 0.5f, worldHeight * 0.5f);
        batch = new SpriteBatch();
        
        Rectangle spriteBoundary = new Rectangle(boundaryOffset, 
                                                 boundaryOffset, 
                                                 worldWidth + boundaryPadding, 
                                                 worldHeight + boundaryPadding);
        ghostManager = new GameObjectPoolManager<Ghost>(100, 2, spriteBoundary)
        {
            @Override
            protected Ghost newGameObject()
            {
                return new Ghost(world, Assets.ghostPumpkin, 2);
            }

            @Override
            protected Vector2 getSpawnCoords()
            {
                Random rand = new Random();
                return new Vector2(rand.nextInt((int)worldWidth), -2.0f);
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
        
        world.step(TIME_STEP, 6, 2);
    }

    @Override
    public void resize(int width, int height)
    {
        float viewportWidth;
        float viewportHeight;
        float aspectRatio;
        
        if(width < height)
        {
            viewportWidth = width * (worldHeight / height);
            viewportHeight = worldHeight;
        }
        else
        {
            viewportWidth = height * (worldHeight / width);
        }

        aspectRatio = (float)width / (float)height;
        viewportHeight = (width < height) ? worldHeight : viewportWidth;
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
        Gdx.app.log("Offset", "[" + x + ", " + y + "]");
        camera.setOffset(x, y);
    }
}
