package com.machinemode.lwp.halloween.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Ghost
{
    private Body body; 
    private Sprite sprite;   
    
    public Ghost(World world, TextureRegion textureRegion, Vector2 position, float diameter)
    {
        sprite = new Sprite(textureRegion);
    }
}
