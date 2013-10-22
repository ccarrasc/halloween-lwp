package com.machinemode.lwp.spookypooky.background;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ParallaxLayer
{
    private Sprite sprite;
    private Vector2 ratio;

    public ParallaxLayer(TextureRegion textureRegion,
                         Vector2 parallaxRatio,
                         float x,
                         float y,
                         float width,
                         float height,
                         float degrees)
    {
        ratio = new Vector2(parallaxRatio);
        sprite = new Sprite(textureRegion);
        sprite.setBounds(x, y, width, height);
        sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
        sprite.setRotation(degrees);
    }

    public void render(SpriteBatch spriteBatch, Vector3 cameraOffset)
    {
        sprite.translate(cameraOffset.x * ratio.x, cameraOffset.y * ratio.y);
        sprite.draw(spriteBatch);
    }
}
