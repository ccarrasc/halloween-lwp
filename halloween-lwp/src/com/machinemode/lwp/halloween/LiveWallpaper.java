package com.machinemode.lwp.halloween;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.machinemode.lwp.halloween.background.BackgroundMesh;
import com.machinemode.lwp.halloween.physics.SimpleWorld;

public class LiveWallpaper implements ApplicationListener
{
    private World world;
    private final float worldHeight = 10.0f;
    private float worldWidth;
    private Vector2 gravity = new Vector2(0f, 0.02f);
    private static final float TIME_STEP = 1f / 45f;

    // Camera
    private OrthographicCamera camera = new OrthographicCamera();
    private float viewWidth, viewHalfWidth;
    private float viewHeight, viewHalfHeight;
    private float aspectRatio;
    private static Vector2 scrollOffset = new Vector2();
    private Vector3 cameraOffset = new Vector3();
    private Vector3 cameraPrevious = new Vector3();

    // Background
    BackgroundMesh backgroundMesh;

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
        backgroundMesh = new BackgroundMesh(20f, 20f, worldWidth / 2f, worldHeight / 2f);
        batch = new SpriteBatch();
    }

    @Override
    public void resume()
    {
        // TODO
    }

    @Override
    public void dispose()
    {
        batch.dispose();
    }

    @Override
    public void render()
    {
        updateCamera();

        Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        backgroundMesh.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

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
        if(width < height)
        {
            viewWidth = width * (worldHeight / height);
            viewHeight = worldHeight;
        }
        else
        {
            viewWidth = height * (worldHeight / width);
        }

        aspectRatio = (float)width / (float)height;
        viewHeight = (width < height) ? worldHeight : viewWidth;
        viewHalfWidth = viewWidth * 0.5f;
        viewHalfHeight = viewHeight * 0.5f;

        camera.setToOrtho(false, viewHeight * aspectRatio, viewHeight);
        camera.position.set(viewHalfWidth + (viewWidth * scrollOffset.x), viewHalfHeight, 0);

        // reset the previous position
        cameraPrevious.set(camera.position);
        backgroundMesh.updateVertices(new Vector2(worldWidth * 0.5f, viewHalfHeight));
    }

    @Override
    public void pause()
    {
        // TODO
    }

    public static void setOffset(float x, float y)
    {
        scrollOffset.set(x, y);
    }

    private void updateCamera()
    {
        cameraPrevious.set(camera.position);
        camera.position.set(viewHalfWidth + (viewWidth * scrollOffset.x), 
                            viewHalfHeight, 
                            0);
        camera.update();
        cameraOffset.set(camera.position.x - cameraPrevious.x,
                         camera.position.y - cameraPrevious.y,
                         0);
    }

    private void updateGravity()
    {
        float x = Gdx.input.getAccelerometerX() * 0.01f;
        float y = Gdx.input.getAccelerometerY() * 0.01f;

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

    private void processTouchEvent()
    {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        /*
         * for(Pumpkin pumpkin : pumpkins) { if(pumpkin.isCollision(touchPos.x, touchPos.y)) { // do
         * domething break; } }
         */
    }
}
