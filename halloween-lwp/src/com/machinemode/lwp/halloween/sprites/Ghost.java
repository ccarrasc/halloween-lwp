package com.machinemode.lwp.halloween.sprites;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pool.Poolable;

public final class Ghost implements GameObject, Poolable
{
    private static final float MAX_RADIAN_ANGLE = 0.5f;
    private static final int MAX_IMPULSE = 10;
    private static final float ROTATION_STEP = 0.001f;
    private final Body body;
    private final Sprite sprite;
    private float radius;
    private boolean rotate;
    private boolean rotateClockwise;

    public static class Builder implements SpriteBuilder<Ghost>
    {
        private final TextureRegion textureRegion;
        private final float diameter;
        private float density;
        private float friction;
        private float restitution;
        private float angularDamping;
        private float linearDamping;
        private boolean rotate;
        
        public Builder(TextureRegion textureRegion, float diameter)
        {
            this.textureRegion = textureRegion;
            this.diameter = diameter;
        }
        
        public Builder density(float value)
        {
            density = value;
            return this;
        }
        
        public Builder friction(float value)
        {
            friction = value;
            return this;
        }
        
        public Builder restitution(float value)
        {
            restitution = value;
            return this;
        }
        
        public Builder angularDamping(float value)
        {
            angularDamping = value;
            return this;
        }
        
        public Builder linearDamping(float value)
        {
            linearDamping = value;
            return this;
        }
        
        public Builder rotate(boolean value)
        {
            rotate = value;
            return this;
        }

        @Override
        public Ghost build(World world)
        {
            return new Ghost(world, this);
        }
    }
    
    @SuppressWarnings("synthetic-access")
    Ghost(World world, Builder builder)
    {
        radius = builder.diameter * 0.5f;
        sprite = new Sprite(builder.textureRegion);
        sprite.setBounds(0, 0, builder.diameter, builder.diameter);
        sprite.setOrigin(radius, radius);   // center = [width/2, height/2]
        rotate = builder.rotate;
        body = createBody(world, builder);
    }
    
    @Override
    public Vector2 getPosition()
    {
        return body.getPosition();
    }
    
    @Override
    public void init(Vector2 position, float angle)
    {
        Random rand = new Random();
        body.setAwake(true);
        body.setTransform(position, angle);
        body.applyLinearImpulse(rand.nextInt(MAX_IMPULSE) - MAX_IMPULSE/2, 
                                rand.nextInt(MAX_IMPULSE), 
                                position.x, 
                                position.y);
    }
    
    @Override
    public void render(SpriteBatch spriteBatch)
    {     
        sprite.setPosition(body.getPosition().x - sprite.getOriginX(), 
                           body.getPosition().y - sprite.getOriginY());
        doRotation();
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
    public void reset()
    {
        body.setAwake(false);  
    }
    
    @SuppressWarnings("synthetic-access")
    private Body createBody(World world, Builder builder)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.angularDamping = builder.angularDamping;
        bodyDef.linearDamping = builder.linearDamping;
        
        CircleShape circle = new CircleShape();
        circle.setRadius(radius); 

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = builder.density; 
        fixtureDef.friction = builder.friction;
        fixtureDef.restitution = builder.restitution; 
        
        Body circularBody = world.createBody(bodyDef);
        circularBody.createFixture(fixtureDef);
        circularBody.setUserData(this);
        circularBody.resetMassData();
        
        circle.dispose();
        
        return circularBody;
    }
    
    private void doRotation()
    {
        float angle = body.getAngle();
        
        if(rotate || angle != 0)
        {
            if(angle < -MAX_RADIAN_ANGLE)
            {
                rotateClockwise = false;
            }
            else if(angle > MAX_RADIAN_ANGLE)
            {
                rotateClockwise = true;
            }
            
            if(rotateClockwise)
            {
                angle -= ROTATION_STEP;
            }
            else
            {
                angle += ROTATION_STEP;
            }
        
            body.setTransform(body.getPosition(), angle);
        }       
    }
}
