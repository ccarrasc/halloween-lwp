package com.machinemode.lwp.halloween.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Ghost implements Poolable
{
    private Body body;
    private Sprite sprite;
    private float radius;

    public Ghost(World world, TextureRegion textureRegion, float diameter)
    {
        radius = diameter * 0.5f;

        sprite = new Sprite(textureRegion);
        sprite.setBounds(0, 0, diameter, diameter);
        sprite.setOrigin(radius, radius); // center = [ width/2, height/2]

        body = createCircularBody(world, radius);
    }

    public void init(Vector2 position)
    {
        body.getPosition().set(position);
    }
    
    @Override
    public void reset()
    {
        body.setTransform(new Vector2(6.5f, 3f), 0);
        body.setLinearVelocity(0, 0);
    }
    
    public void render(SpriteBatch spriteBatch)
    {
        sprite.setPosition(body.getPosition().x - sprite.getOriginX(), 
                           body.getPosition().y - sprite.getOriginY());
        sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        sprite.draw(spriteBatch);
    }

    public Vector2 getPosition()
    {
        return body.getPosition();
    }
    
    public boolean isCollision(float x, float y)
    {
        Vector2 center = body.getWorldCenter();
        if(x >= center.x - radius && 
           x <= center.x + radius && 
           y >= center.y - radius && 
           y <= center.y + radius)
        {
            return true;
        }

        return false;
    }

    private Body createCircularBody(World world, float radius)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        //bodyDef.angularDamping = 0.9f;
        //bodyDef.linearDamping = 1.0f;
        Body circularBody = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.3f; 
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.3f; 
        circularBody.createFixture(fixtureDef);
        circle.dispose();
        circularBody.setUserData(this);
        return circularBody;
    }
}
