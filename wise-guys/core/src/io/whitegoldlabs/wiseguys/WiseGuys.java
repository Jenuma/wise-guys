package io.whitegoldlabs.wiseguys;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Assets;
import io.whitegoldlabs.wiseguys.util.ConsoleCommandExecutor;
import io.whitegoldlabs.wiseguys.util.Mappers;
import io.whitegoldlabs.wiseguys.util.ScriptManager;
import io.whitegoldlabs.wiseguys.view.GameScreen;
import io.whitegoldlabs.wiseguys.view.MainMenuScreen;

public class WiseGuys extends Game
{
	public Assets assets;
	public ScriptManager scriptManager;
	
	public Screen currentScreen;
	public String currentWorld;
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	public BitmapFont font;
	public BitmapFont bigFont;
	
	public Engine engine;
	public Entity player;
	
	public Console console;
	
	public boolean eventProcessing;
	public boolean isRunning;
	public boolean wasSleeping;
	
	public String nextGameScreenDestination;
	private float nextGameScreenX;
	private float nextGameScreenY;
	
	private Texture spriteSheet;
	
	@Override
	public void create()
	{
		this.assets = new Assets();
		assets.manager.load(Assets.spriteSheet);
		assets.manager.finishLoading();
		
		this.camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom -= 0.7f;
		
		this.spriteSheet = assets.manager.get(Assets.spriteSheet);
		this.batch = new SpriteBatch();
		this.font = new BitmapFont(Gdx.files.internal("pressstart2p.fnt"));
		this.bigFont = new BitmapFont(Gdx.files.internal("pressstart2p.fnt"));
		bigFont.getData().setScale(2);
		
		this.scriptManager = new ScriptManager();
		this.engine = new Engine();
		initPlayer();
		
		this.eventProcessing = false;
		this.isRunning = true;
		this.wasSleeping = false;
		
		this.currentWorld = "world1-1";
		
		this.console = new GUIConsole();
		console.setCommandExecutor(new ConsoleCommandExecutor(this));
		console.setPosition(0, Gdx.graphics.getHeight() / 2);
		console.setSizePercent(100, 50);
		console.setDisplayKeyID(Input.Keys.GRAVE);
		
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
	
	public void prepareNextGameScreen(String worldName, float x, float y)
	{
		this.nextGameScreenDestination = worldName;
		this.nextGameScreenX = x;
		this.nextGameScreenY = y;
	}
	
	public void setNextGameScreen()
	{
		GameScreen nextGameScreen = new GameScreen(this, nextGameScreenDestination, nextGameScreenX, nextGameScreenY);
		
		currentScreen.dispose();
		currentScreen = nextGameScreen;
		screen = nextGameScreen;
	
		nextGameScreenDestination = null;
	}
	
	private void initPlayer()
	{
		this.player = new Entity();
		
		Sprite playerHitboxSprite = new Sprite(spriteSheet, 144, 144, 16, 16);
		
		StateComponent stateComponent = new StateComponent();
		stateComponent.directionState = StateComponent.DirectionState.RIGHT;
		stateComponent.airborneState = StateComponent.AirborneState.GROUNDED;
		
		PlayerComponent playerComponent = new PlayerComponent(0, 0, 3);
		
	    player.add(playerComponent);
	    player.add(stateComponent);
	    player.add(new SpriteComponent(new Sprite(spriteSheet, 0, 0, 16, 16)));
	    player.add(new TypeComponent(TypeComponent.Type.PLAYER));
		player.add(new PositionComponent(0, 0));
		player.add(new VelocityComponent(0, 0));
		player.add(new AccelerationComponent(0, 0));
		player.add(new HitboxComponent
		(
			0,
			0,
			16,
			16,
			playerHitboxSprite
		));
		
		setPlayerAnimations(playerComponent.playerState);
	}
	
	private void setPlayerAnimations(PlayerComponent.PlayerState state)
	{
		Sprite stillSprite;
		Sprite jumpingSprite;
		Sprite moving1Sprite;
		Sprite moving2Sprite;
		Sprite moving3Sprite;
		
		Array<Sprite> stillSprites = new Array<>(); 
		Array<Sprite> jumpingSprites = new Array<>();
		Array<Sprite> movingSprites = new Array<>();
		Array<Sprite> slowingSprites = new Array<>();
		Array<Sprite> damagedSprites = new Array<>();
		Array<Sprite> powerupSprites = new Array<>();
		
		switch(state)
		{
			case NORMAL:
				stillSprite = new Sprite(spriteSheet, 0, 0, 16, 16);
				jumpingSprite = new Sprite(spriteSheet, 64, 0, 16, 16);
				moving1Sprite = new Sprite(spriteSheet, 16, 0, 16, 16);
				moving2Sprite = new Sprite(spriteSheet, 32, 0, 16, 16);
				moving3Sprite = new Sprite(spriteSheet, 48, 0, 16, 16);
				break;
				
			case SUPER:
				stillSprite = new Sprite(spriteSheet, 0, 16, 16, 32);
				jumpingSprite = new Sprite(spriteSheet, 64, 16, 16, 32);
				moving1Sprite = new Sprite(spriteSheet, 16, 16, 16, 32);
				moving2Sprite = new Sprite(spriteSheet, 32, 16, 16, 32);
				moving3Sprite = new Sprite(spriteSheet, 48, 16, 16, 32);
				break;
				
			default:
				stillSprite = null;
				jumpingSprite = null;
				moving1Sprite = null;
				moving2Sprite = null;
				moving3Sprite = null;
		}
		
		stillSprites.add(stillSprite);
		jumpingSprites.add(jumpingSprite);
		movingSprites.add(moving1Sprite);
		movingSprites.add(moving2Sprite);
		movingSprites.add(moving3Sprite);
		slowingSprites.add(stillSprite);
		damagedSprites.add(stillSprite);
		damagedSprites.add(new Sprite(spriteSheet, 0, 0, 0, 0));
		
		Sprite playerMedSprite = new Sprite(spriteSheet, 80, 16, 16, 32);
		Sprite playerBigSprite = new Sprite(spriteSheet, 0, 16, 16, 32);
		powerupSprites.add(playerMedSprite);
		powerupSprites.add(stillSprite);
		powerupSprites.add(playerMedSprite);
		powerupSprites.add(stillSprite);
		powerupSprites.add(playerMedSprite);
		powerupSprites.add(playerBigSprite);
		powerupSprites.add(stillSprite);
		powerupSprites.add(playerMedSprite);
		powerupSprites.add(playerBigSprite);
		powerupSprites.add(stillSprite);
		
		AnimationComponent animation = new AnimationComponent();
		animation.animations.put("STILL", new Animation<Sprite>(1f/32f, stillSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("MOVING", new Animation<Sprite>(1f/32f, movingSprites, Animation.PlayMode.LOOP));
		animation.animations.put("SLOWING", new Animation<Sprite>(1f/32f, slowingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("JUMPING", new Animation<Sprite>(1f/32f, jumpingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("DAMAGED", new Animation<Sprite>(1f/32f, damagedSprites, Animation.PlayMode.LOOP));
		animation.animations.put("POWERUP", new Animation<Sprite>(1f/16f, powerupSprites, Animation.PlayMode.NORMAL));
		player.add(animation);
	}
	
	public void powerupSuperJules()
	{
		Mappers.hitbox.get(player).hitbox.height = 32;
		PlayerComponent playerComponent = Mappers.player.get(player);
		
		playerComponent.playerState = PlayerComponent.PlayerState.SUPER;
		
		setPlayerAnimations(playerComponent.playerState);
	}
	
	public void powerdownNormalJules()
	{
		Mappers.hitbox.get(player).hitbox.height = 16;
		PlayerComponent playerComponent = Mappers.player.get(player);
		
		playerComponent.playerState = PlayerComponent.PlayerState.NORMAL;
		playerComponent.damaged = true;
		
		setPlayerAnimations(playerComponent.playerState);
	}
}
