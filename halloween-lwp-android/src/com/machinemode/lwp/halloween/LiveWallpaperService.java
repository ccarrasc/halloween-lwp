package com.machinemode.lwp.halloween;

import android.view.SurfaceHolder;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;

public class LiveWallpaperService extends AndroidLiveWallpaperService
{
    private LiveWallpaper liveWallpaper;

    @Override
    public AndroidApplicationConfiguration createConfig()
    {
        return new AndroidApplicationConfiguration();
    }

    @Override
    public ApplicationListener createListener(boolean arg0)
    {
        liveWallpaper = new LiveWallpaper();
        return liveWallpaper;
    }

    @Override
    public void offsetChange(ApplicationListener listener,
                             float xOffset, float yOffset,
                             float xOffsetStep, float yOffsetStep,
                             int xPixelOffset, int yPixelOffset)
    {
        LiveWallpaper.setOffset(xOffset, yOffset);
    }

    @Override
    public Engine onCreateEngine()
    {
        AndroidWallpaperEngine engine = new AndroidWallpaperEngine()
        {
            @Override
            public void onSurfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height)
            {
                super.onSurfaceChanged(holder, format, width, height);
            }
        };

        engine.setTouchEventsEnabled(true);
        return engine;
    }
}
