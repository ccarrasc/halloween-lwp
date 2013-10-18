package com.machinemode.lwp.halloween;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.machinemode.lwp.halloween.background.BackgroundMesh;
import com.machinemode.lwp.halloween.physics.SimpleWorld;
import com.machinemode.lwp.halloween.sprites.Ghost;

public class LiveWallpaper implements ApplicationListener
{
    protected World world;
    private final float worldHeight = 10.0f;
    private float worldWidth;
    private Vector2 gravity = new Vector2();
    private static final float TIME_STEP = 1f / 45f;
    
    private static WallpaperCamera camera = new WallpaperCamera();
    
    // Background
    BackgroundMesh backgroundMesh;

    private List<Ghost> activeGhosts = new ArrayList<Ghost>();
    private Pool<Ghost> ghostPool = new Pool<Ghost>()
    {
        @Override
        protected Ghost newObject()
        {
            return new Ghost(world, Assets.ghostPumpkin, 1);
        }   
    };
    
    private SpriteBatch batch;

    @Override
    public void create()
    {
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
        Assets.load();
        backgroundMesh = new BackgroundMesh(30f, 30f, worldWidth * 0.5f, worldHeight * 0.5f);
        batch = new SpriteBatch();
        Ghost ghost = ghostPool.obtain();
        ghost.init(new Vector2(2, 2));
        activeGhosts.add(ghost);
    }

    @Override
    public void resume()
    {
        // TODO
    }

    @Override
    public void dispose()
    {
//        Assets.dispose();
//        backgroundMesh.dispose();
//        batch.dispose();
//        world.dispose();
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
        
        for(int i = 0; i < activeGhosts.size(); ++i)
        {
            Ghost ghost = activeGhosts.get(i);
            if(ghost.getPosition().y > worldHeight)
            {
                ghost.reset();
            }
            
            ghost.render(batch);
        }
        
        batch.end();

        if(Gdx.input.justTouched())
        {
            processTouchEvent();
        }

        // Update physics
        updateGravity();
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
        camera.setOffset(x, y);
    }
    
    private void updateGravity()
    {
        float x = Gdx.input.getAccelerometerX() * 0.001f;
        float y = Gdx.input.getAccelerometerY() * 0.001f;
        
        switch(Gdx.input.getRotation())
        {
            case 0: // Surface.ROTATION_0
                gravity = new Vector2(x, y);
                break;
            case 1: // Surface.ROTATION_90
                gravity = new Vector2(-y, x);
                break;
            case 2: // Surface.ROTATION_180
                gravity = new Vector2(-x, -y);
                break;
            case 3: // Surface.ROTATION_270
                gravity = new Vector2(y, -x);
                break;
            default:
                break;
        }

        world.setGravity(gravity);
    }

    private static void processTouchEvent()
    {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        Gdx.app.log("Touch", touchPos.toString());
        Gdx.app.log("Rotation", String.valueOf(Gdx.input.getRotation()));
        Gdx.app.log("X", String.valueOf(Gdx.input.getAccelerometerX()));
        Gdx.app.log("Y", String.valueOf(Gdx.input.getAccelerometerY()));
    }
}
