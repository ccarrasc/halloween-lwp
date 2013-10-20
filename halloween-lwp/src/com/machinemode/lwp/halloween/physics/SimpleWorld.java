package com.machinemode.lwp.halloween.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class SimpleWorld
{
    /**
     * Static contact listener. Called every time a collision occurs.
     */
    private static ContactListener contactListener = new ContactListener()
    {
        /**
         * This event is called when two fixtures begin to overlap or start to collide. Usually we
         * would check here if the body is to be removed or not.
         */
        @Override
        public void beginContact(Contact contact)
        {
            Body a = contact.getFixtureA().getBody();
            Body b = contact.getFixtureB().getBody();

            if(a.getUserData() != null)
            {
                // Do something with a
            }

            if(b.getUserData() != null)
            {
                // Do something with b
            }
        }

        /**
         * This event is called when the overlap between the two fixtures ends. This can be called
         * when we delete a body/fixture as the contact would stop existing for that body/fixture.
         */
        @Override
        public void endContact(Contact contact)
        {
            // TODO Auto-generated method stub
        }

        /**
         * This is called after the collision is detected but before the response has been
         * calculated. This is good if we don’t want the object to behave in normal way for e.g.
         * maybe tunnel through a wall from one direction and not from another.
         */
        @Override
        public void preSolve(Contact contact, Manifold oldManifold)
        {
            contact.setEnabled(false);
        }

        /**
         * In this event we get the collision impulse results. But mostly if we would like to change
         * the collision response we would do it in PreSolve.
         */
        @Override
        public void postSolve(Contact contact, ContactImpulse impulse)
        {
            // TODO Auto-generated method stub
        }
    };
    
    public static World newWorld(Vector2 gravity)
    {
        World world = new World(gravity, true);
        world.setContactListener(contactListener);  
        return world;
    }
}
