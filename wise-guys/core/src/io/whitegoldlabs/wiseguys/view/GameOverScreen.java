package io.whitegoldlabs.wiseguys.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.constant.Constants;

public class GameOverScreen implements Screen
{
	final WiseGuys game;
	OrthographicCamera camera;
	
	public GameOverScreen(final WiseGuys game)
	{
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}

	@Override
	public void show()
	{
		
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.42f, 0.55f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
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
