package com.machinemode.lwp.halloween.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector2;

public class BackgroundMesh
{
    private static final int VERTEX_COUNT = 6;
    private static final int INDEX_COUNT = 6;

    private Mesh mesh;
    private float halfWidth;
    private float halfHeight;
    private Vector2 center = new Vector2();
    private int r = 191;
    private int g = 81;
    private int b = 7;
    private int a = 127;
    
    public BackgroundMesh(float w, float h, float x, float y)
    {
        center.x = x;
        center.y = y;
        halfWidth = w * 0.5f;
        halfHeight = h * 0.5f;

        mesh = new Mesh(true,
                        VERTEX_COUNT,
                        INDEX_COUNT,
                        new VertexAttribute(Usage.Position, 3, "a_position"),  //$NON-NLS-1$
                        new VertexAttribute(Usage.ColorPacked, 4, "a_color")); //$NON-NLS-1$

        loadVertices();
        mesh.setIndices(new short[] { 0, 1, 2, 3, 4, 5 });
    }

    public void dispose()
    {
        mesh.dispose();
    }
    
    public void render()
    {
        mesh.render(GL20.GL_TRIANGLE_STRIP, 0, VERTEX_COUNT);
    }

    public void updateVertices(Vector2 center)
    {
        this.center = center;
        loadVertices();
    }
    
    private void loadVertices()
    {
        mesh.setVertices(new float[] {center.x - halfWidth,
                                      center.y + halfHeight,
                                      0,
                                      Color.toFloatBits(r, g, b, a), // 0
                                      center.x - halfWidth,
                                      center.y - halfHeight,
                                      0,
                                      Color.toFloatBits(r, g, b, a), // 1
                                      center.x,
                                      center.y + halfHeight,
                                      0,
                                      Color.toFloatBits(0, 0, 0, a), // 2
                                      center.x,
                                      center.y - halfHeight,
                                      0,
                                      Color.toFloatBits(0, 0, 0, a), // 3
                                      center.x + halfWidth,
                                      center.y + halfHeight,
                                      0,
                                      Color.toFloatBits(r, g, b, a), // 4
                                      center.x + halfWidth,
                                      center.y - halfHeight,
                                      0,
                                      Color.toFloatBits(r, g, b, a), // 5
        });
    }
}
