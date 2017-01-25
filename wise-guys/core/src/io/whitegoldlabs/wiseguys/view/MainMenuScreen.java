package io.whitegoldlabs.wiseguys.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.util.Assets;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class MainMenuScreen implements Screen
{
	final WiseGuys game;
	
	Stage stage;
	Table table;
	VerticalGroup modeGroup;
	
	Image selector;
	Sound sfxPop;
	
	boolean arcadeSelected;
	
	// ---------------------------------------------------------------------------------|
	// Constructors                                                                     |
	// ---------------------------------------------------------------------------------|
	public MainMenuScreen(final WiseGuys game)
	{
		this.game = game;
		
		game.assets.manager.load(Assets.title);
		game.assets.manager.load(Assets.sfxPop);
		game.assets.manager.finishLoading();
		
		this.stage = new Stage();
		this.table = new Table();
		
		table.setFillParent(true);
	    stage.addActor(table);
	    
	    // Title Image
	    Image title = new Image(game.assets.manager.get(Assets.title));
	    title.setSize(title.getWidth()*3, title.getHeight()*3);
	    title.setPosition((Gdx.graphics.getWidth() - title.getWidth()) / 2, (Gdx.graphics.getHeight() - title.getHeight()) / 2);
	    stage.addActor(title);
	    
	    Skin skin = new Skin(Gdx.files.internal("uiskin.json"),
    		new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
	    
	    // Labels
	    Label lblArcade = new Label("Arcade Mode", skin, "default");
	    Label lblStageSelect = new Label("Select Stage", skin, "default");
	    
	    modeGroup = new VerticalGroup();
	    modeGroup.addActor(lblArcade);
	    modeGroup.addActor(lblStageSelect);
	    modeGroup.setPosition((Gdx.graphics.getWidth() - modeGroup.getWidth()) / 2, 180);
	    modeGroup.space(5);
	    stage.addActor(modeGroup);
	    
	    TextureRegion selectorTexture = new TextureRegion(new Texture(Gdx.files.internal("uiskin.png")), 0, 0, 10, 19);
	    selector = new Image(selectorTexture);
	    selector.setSize(selector.getWidth() * 1.5f, selector.getHeight() * 1.5f);
	    selector.setRotation(90);
	    selector.setPosition(modeGroup.getX() - 100, 168);
	    stage.addActor(selector);
	    
	    sfxPop = game.assets.manager.get(Assets.sfxPop);
	    
	    arcadeSelected = true;
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
		
	    if(Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.UP))
	    {
	    	sfxPop.play();
	    	arcadeSelected = !arcadeSelected;
	    }
	    
	    if(Gdx.input.isKeyJustPressed(Keys.Z))
	    {
	    	PlayerComponent playerInventory = Mappers.player.get(game.player);
	    	playerInventory.score = 0;
	    	playerInventory.coins = 0;
	    	playerInventory.lives = 3;
	    	playerInventory.time = 400;
		    
	    	if(arcadeSelected)
	    	{
	    		sfxPop.play();
	    		
	    		WorldIntroScreen worldIntroScreen = new WorldIntroScreen(game, "world1-1");
				game.currentScreen = worldIntroScreen;
				game.setScreen(game.currentScreen);
				dispose();
	    	}
	    	else
	    	{
	    		System.out.println("Not implemented!");
	    	}
	    }
	    
	    if(arcadeSelected)
	    {
	    	selector.setPosition(modeGroup.getX() - 100, 168);
	    }
	    else
	    {
	    	selector.setPosition(modeGroup.getX() - 100, 148);
	    }
	    
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
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
