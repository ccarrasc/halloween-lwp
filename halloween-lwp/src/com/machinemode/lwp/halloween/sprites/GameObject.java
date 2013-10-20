package com.machinemode.lwp.halloween.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

// TODO: make interface (rethink whold thing_
public abstract class GameObject
{
    protected Body body;
    protected Sprite sprite;
    
    public GameObject(World world, TextureRegion textureRegion, float width, float height)
    {
        sprite = new Sprite(textureRegion);
        sprite.setBounds(0, 0, width, height);
        sprite.setOrigin(width * 0.5f, height * 0.5f);
        body = createBody(world, width, height);
    }
    
    public Vector2 getPosition()
    {
        return body.getPosition();
    }
    
    abstract public void init(Vector2 position);
    
    abstract public void render(SpriteBatch spriteBatch);
        
    abstract public boolean isCollision(float x, float y);
    
    abstract protected Body createBody(World world, float width, float height);
}
