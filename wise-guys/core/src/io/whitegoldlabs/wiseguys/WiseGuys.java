package io.whitegoldlabs.wiseguys;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.whitegoldlabs.wiseguys.view.MainMenu;

public class WiseGuys extends Game
{
	public SpriteBatch batch;
	public BitmapFont font;
	
	@Override
	public void create()
	{
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("pressstart2p.fnt"));
		font.setColor(0.2f, 0.3f, 0, 1);
		
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render()
	{
		super.render();
	}
	
	@Override
	public void dispose()
	{
		batch.dispose();
		font.dispose();
	}
}
