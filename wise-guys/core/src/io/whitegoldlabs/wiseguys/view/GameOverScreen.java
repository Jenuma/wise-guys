package io.whitegoldlabs.wiseguys.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.constant.Constants;
import io.whitegoldlabs.wiseguys.util.Assets;

public class GameOverScreen implements Screen
{
	final WiseGuys game;
	OrthographicCamera camera;
	
	float time;
	
	public GameOverScreen(final WiseGuys game)
	{
		this.game = game;
		
		game.assets.manager.load(Assets.sfxGameOver);
		game.assets.manager.finishLoading();
		
		this.time = 0;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}

	@Override
	public void show()
	{
		game.assets.manager.get(Assets.sfxGameOver).play();
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        time += delta;
        
        if(time >= 4)
        {
        	MainMenuScreen mainMenuScreen = new MainMenuScreen(game);
    		game.currentScreen = mainMenuScreen;
    		game.setScreen(game.currentScreen);
    		dispose();
        }
        
        camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.bigFont.draw(game.batch, "Game Over", Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2);
		
		game.batch.end();
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
