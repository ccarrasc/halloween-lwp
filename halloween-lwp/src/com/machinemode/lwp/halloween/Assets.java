package com.machinemode.lwp.halloween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Assets
{
    protected static TextureRegion ghost;
    
    /**
     * Suppressed to prevent instantiation
     */
    private Assets()
    {
        throw new AssertionError();
    }
    
    public static void load()
    {
        Texture texture = new Texture(Gdx.files.internal("sprites/ghost-v1.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        ghost = new TextureRegion(texture, 0, 0, 256, 256);
    }
    
    public static void dispose()
    {
        ghost.getTexture().dispose();
    }
}
