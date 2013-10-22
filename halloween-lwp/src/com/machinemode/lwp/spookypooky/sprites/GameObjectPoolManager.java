package com.machinemode.lwp.spookypooky.sprites;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public abstract class GameObjectPoolManager<T extends GameObject>
{
    protected List<T> activeObjects;
    protected Pool<T> pool;
    private int maxActive;
    private int maxSpawnDelay;
    private static Rectangle spriteBoundary;
    private Task spawn = new Task()
    {
        @Override
        public void run()
        {
            T gameObject = pool.obtain();
            gameObject.init(getPosition(), getAngle());
            activeObjects.add(gameObject);
        }
    };

    public GameObjectPoolManager(int max, int maxDelay, Rectangle boundary)
    {
        maxActive = max;
        maxSpawnDelay = maxDelay;
        spriteBoundary = boundary;
        activeObjects = new ArrayList<T>(maxActive);
        pool = new Pool<T>(maxActive, maxActive * 2)
        {
            @Override
            protected T newObject()
            {
                return newGameObject();
            }
        };
    }

    protected abstract T newGameObject();

    protected abstract Vector2 getPosition();

    protected abstract float getAngle();

    public void update(SpriteBatch spriteBatch)
    {
        int length = activeObjects.size();
        for(int i = length - 1; i >= 0; --i)
        {
            T gameObject = activeObjects.get(i);
            Vector2 position = gameObject.getPosition();
            if(!spriteBoundary.contains(position.x, position.y))
            {
                activeObjects.remove(i);
                pool.free(gameObject);
            }
            else
            {
                gameObject.render(spriteBatch);
            }
        }

        if(activeObjects.size() < maxActive && !spawn.isScheduled())
        {
            Random rand = new Random();
            float delay = rand.nextInt(maxSpawnDelay);
            Timer.schedule(spawn, delay);
        }
    }
}
