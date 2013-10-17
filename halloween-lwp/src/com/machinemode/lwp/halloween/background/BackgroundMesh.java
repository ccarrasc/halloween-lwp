package com.machinemode.lwp.halloween.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector2;

public class BackgroundMesh
{
    private static final int vertexCount = 6;
    private static final int indexCount = 6;

    private Mesh mesh;
    private float halfWidth;
    private float halfHeight;
    private Vector2 center = new Vector2();
    private int r = 0;
    private int g = 0;
    private int b = 0;
    private int a = 127;
    
    public BackgroundMesh(float w, float h, float x, float y)
    {
        center.x = x;
        center.y = y;
        halfWidth = w * 0.5f;
        halfHeight = h * 0.5f;

        mesh = new Mesh(true,
                        vertexCount,
                        indexCount,
                        new VertexAttribute(Usage.Position, 3, "a_position"), //$NON-NLS-1$
                        new VertexAttribute(Usage.ColorPacked, 4, "a_color")); //$NON-NLS-1$

        loadVertices();

        mesh.setIndices(new short[] { 0, 1, 2, 3, 4, 5 });
    }

    public void updateVertices(Vector2 center)
    {
        this.center = center;
        loadVertices();
    }

    public void render()
    {
        mesh.render(GL20.GL_TRIANGLE_STRIP, 0, vertexCount);
    }

    private void loadVertices()
    {
        mesh.setVertices(new float[] {center.x - halfWidth,
                                      center.y + halfHeight,
                                      0,
                                      Color.toFloatBits(r + 10, g + 1, b + 1, a), // 0
                                      center.x - halfWidth,
                                      center.y - halfHeight,
                                      0,
                                      Color.toFloatBits(r - 1, g - 1, b - 1, a), // 1
                                      center.x,
                                      center.y + halfHeight,
                                      0,
                                      Color.toFloatBits(r + 2, g + 20, b + 2, a), // 2
                                      center.x,
                                      center.y - halfHeight,
                                      0,
                                      Color.toFloatBits(r, g, b, a), // 3
                                      center.x + halfWidth,
                                      center.y + halfHeight,
                                      0,
                                      Color.toFloatBits(r - 3, g - 30, b - 3, a), // 4
                                      center.x + halfWidth,
                                      center.y - halfHeight,
                                      0,
                                      Color.toFloatBits(r + 10, g + 10, b + 1, a), // 5
        });
    }
}
