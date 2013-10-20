package com.machinemode.lwp.halloween.sprites;

import java.util.Random;

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

public final class Ghost extends GameObject implements Poolable
{
    private float radius;
       
    public Ghost(World world, TextureRegion textureRegion, float diameter)
    {
        super(world, textureRegion, diameter, diameter);
        radius = diameter * 0.5f;
    }

    @Override
    public void init(Vector2 position)
    {
        Random rand = new Random();
        float x = rand.nextInt(20) - 10;//-1.0f + rand.nextFloat() * 2.0f;
        float y = rand.nextInt(20) - 10;//-1.0f + rand.nextFloat() * 2.0f;
        body.setAwake(true);
        body.setTransform(position, 0);
        body.applyLinearImpulse(new Vector2(x, y), position);
        body.applyAngularImpulse(-1.0f + rand.nextFloat() * 2);
    }
    
    @Override
    public void reset()
    {
        body.setTransform(new Vector2(0.0f, 0.0f), 0);
        body.setLinearVelocity(0, 0);
        body.setAwake(false);
    }
    
    @Override
    public void render(SpriteBatch spriteBatch)
    {
        Random rand = new Random();
        body.applyTorque(-2.0f + rand.nextFloat() * 4);
        sprite.setPosition(body.getPosition().x - sprite.getOriginX(), 
                           body.getPosition().y - sprite.getOriginY());
        sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        sprite.draw(spriteBatch);
    }
    
    @Override
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

    @Override   
    protected Body createBody(World world, float width, float height)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.angularDamping = 0.5f;
        bodyDef.linearDamping = 0.2f;
        
        CircleShape circle = new CircleShape();
        circle.setRadius(width);    // radius not set here as this is called by base class cntr

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.3f; 
        fixtureDef.friction = 0.4f;
        //fixtureDef.restitution = 0.3f; 
        
        Body circularBody = world.createBody(bodyDef);
        circularBody.createFixture(fixtureDef);
        circularBody.setUserData(this);
        circularBody.resetMassData();
        
        circle.dispose();
        
        return circularBody;
    }
}
