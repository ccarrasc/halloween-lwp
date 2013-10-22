package com.machinemode.lwp.spookypooky.background;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ParallaxBackground
{
    private List<ParallaxLayer> layers;

    public ParallaxBackground(List<ParallaxLayer> backgroundLayers)
    {
        layers = backgroundLayers;
    }

    public void render(SpriteBatch spriteBatch, Vector3 cameraOffset)
    {
        for(ParallaxLayer layer : layers)
        {
            layer.render(spriteBatch, cameraOffset);
        }
    }
}
