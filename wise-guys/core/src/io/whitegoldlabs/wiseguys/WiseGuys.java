package io.whitegoldlabs.wiseguys;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.whitegoldlabs.wiseguys.util.ScriptManager;
import io.whitegoldlabs.wiseguys.view.MainMenuScreen;

public class WiseGuys extends Game
{
	public Screen currentScreen;
	
	public SpriteBatch batch;
	
	public BitmapFont font;
	public BitmapFont bigFont;
	
	public Engine engine;
	public Entity player;
	
	public ScriptManager scriptManager;
	
	public boolean wasSleeping;
	
	@Override
	public void create()
	{
		batch = new SpriteBatch();
		
		font = new BitmapFont(Gdx.files.internal("pressstart2p.fnt"));
		
		bigFont = new BitmapFont(Gdx.files.internal("pressstart2p.fnt"));
		bigFont.getData().setScale(2);
		
		engine = new Engine();
		player = new Entity();
		
		scriptManager = new ScriptManager();
		
		wasSleeping = false;
		
		MainMenuScreen mainMenuScreen = new MainMenuScreen(this);
		this.currentScreen = mainMenuScreen;
		this.setScreen(currentScreen);
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
