package io.whitegoldlabs.wiseguys.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import io.whitegoldlabs.wiseguys.WiseGuys;

public class TransitionScreen implements Screen
{
	final WiseGuys game;
	
	private String worldName;
	private float x;
	private float y;
	
	private float time;
	
	public TransitionScreen(final WiseGuys game, String worldName, float x, float y)
	{
		this.game = game;
		
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		
		this.time = 0;
	}
	
	@Override
	public void show()
	{
		
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        time += delta;
        
        if(time >= 0.5)
        {
        	GameScreen nextGameScreen = new GameScreen(game, worldName, x, y);
    		
    		game.currentScreen.dispose();
    		game.currentScreen = nextGameScreen;
    		game.setScreen(nextGameScreen);
        }
	}

	@Override
	public void resize(int width, int height)
	{
		
	}

	@Override
	public void pause()
	{
		
	}

	@Override
	public void resume()
	{
		
	}

	@Override
	public void hide()
	{
		
	}

	@Override
	public void dispose()
	{
		
	}
}
