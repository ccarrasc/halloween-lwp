package com.machinemode.lwp.halloween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Assets
{
    protected static TextureRegion ghostPumpkin;
    
    private Assets()
    {
        // No instantiation
    }
    
    public static void load()
    {
        Texture texture = new Texture(Gdx.files.internal("sprites/ghost-v1.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        ghostPumpkin = new TextureRegion(texture, 0, 0, 256, 256);
    }
    
    public static void dispose()
    {
        ghostPumpkin.getTexture().dispose();
    }
}
