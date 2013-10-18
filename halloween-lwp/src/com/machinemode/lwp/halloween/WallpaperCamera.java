package com.machinemode.lwp.halloween;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class WallpaperCamera extends OrthographicCamera
{
    private Vector3 previousPosition = new Vector3();
    private Vector2 center = new Vector2();
    private Vector2 offset = new Vector2();
    
    public WallpaperCamera()
    {
        super();
    }
    
    public void resize(float width, float height, float aspectRatio)
    {
        viewportWidth = width;
        viewportHeight = height;
        center.set(viewportWidth * 0.5f, viewportHeight * 0.5f);
        setToOrtho(false, viewportHeight * aspectRatio, viewportHeight);
        position.set(center.x + (viewportWidth * offset.x), center.y, 0);
    }
    
    @Override
    public void update()
    {
        previousPosition.set(position);
        position.set(center.x + (viewportWidth * offset.x), center.y, 0);
        super.update();
    }
    
    public void setOffset(float x, float y)
    {
        offset.set(x, y);
    }
}
