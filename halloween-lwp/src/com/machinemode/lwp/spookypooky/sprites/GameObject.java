package com.machinemode.lwp.spookypooky.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface GameObject
{
    Vector2 getPosition();
    
    void init(Vector2 position, float angle);
        
    void render(SpriteBatch spriteBatch);
        
    boolean isCollision(float x, float y);    
}
