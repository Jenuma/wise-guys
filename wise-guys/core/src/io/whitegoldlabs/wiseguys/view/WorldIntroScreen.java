package io.whitegoldlabs.wiseguys.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.util.Assets;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class WorldIntroScreen implements Screen
{
	final WiseGuys game;
	
	Stage stage;
	Table table;
	
	float time;
	
	// ---------------------------------------------------------------------------------|
	// Constructors                                                                     |
	// ---------------------------------------------------------------------------------|
	public WorldIntroScreen(final WiseGuys game, String worldName)
	{
		this.game = game;
		game.currentWorld = worldName;
		
		game.assets.manager.load(Assets.spriteSheet);
		game.assets.manager.finishLoading();
		
		this.time = 0;
		
		this.stage = new Stage();
		this.table = new Table();
		
		table.setFillParent(true);
	    stage.addActor(table);
	    
	    Skin skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
	    
    	PlayerComponent player = Mappers.player.get(game.player);
    	player.time = 400;
	    
	    // World Name
	    Label lblWorldName = new Label("World " + worldName.substring(5), skin, "default");
	    lblWorldName.setPosition((Gdx.graphics.getWidth() - lblWorldName.getWidth()) / 2, ((Gdx.graphics.getHeight() - lblWorldName.getHeight()) / 2) + 30);
	    
	    // Remaining Lives
	    TextureRegion livesTexture =
	    		new TextureRegion(game.assets.manager.get(Assets.spriteSheet), 0, 0, 16, 16);
	    Image imgLives = new Image(livesTexture);
	    Label lblLives = new Label("x " + player.lives, skin, "default");
	    
	    HorizontalGroup livesGroup = new HorizontalGroup();
	    livesGroup.addActor(imgLives);
	    livesGroup.addActor(lblLives);
	    livesGroup.setPosition((Gdx.graphics.getWidth() - livesGroup.getPrefWidth()) / 2, ((Gdx.graphics.getHeight() - livesGroup.getPrefHeight()) / 2) - 30);
	    livesGroup.space(10);
	    
	    stage.addActor(lblWorldName);
	    stage.addActor(livesGroup);
	}

	// ---------------------------------------------------------------------------------|
	// Implemented Methods                                                              |
	// ---------------------------------------------------------------------------------|
	@Override
	public void show()
	{
		
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	    
	    // Resetting the delta here prevents the engine from updating based on longer deltas that resulted from a thread pause.
 		if(game.wasSleeping)
 		{
 			delta = 0;
 			game.wasSleeping = false;
 		}
	    
	    time += delta;
	    
	    if(time >= 2)
	    {
	    	GameScreen newGameScreen = new GameScreen(game, game.currentWorld, 16, 32);
			game.currentScreen = newGameScreen;
			game.setScreen(game.currentScreen);
			dispose();
	    }
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
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
		stage.dispose();
	}
}
