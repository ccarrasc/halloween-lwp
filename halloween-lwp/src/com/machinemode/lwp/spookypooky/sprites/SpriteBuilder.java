package com.machinemode.lwp.spookypooky.sprites;

import com.badlogic.gdx.physics.box2d.World;

public interface SpriteBuilder<T>
{
    T build(World world);
}
