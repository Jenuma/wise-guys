package io.whitegoldlabs.wiseguys.view;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AirborneStateComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.FacingDirectionStateComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.InventoryComponent;
import io.whitegoldlabs.wiseguys.component.MovingStateComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.system.AnimationSystem;
import io.whitegoldlabs.wiseguys.system.CollisionSystem;
import io.whitegoldlabs.wiseguys.system.DebugRenderSystem;
import io.whitegoldlabs.wiseguys.system.GravitySystem;
import io.whitegoldlabs.wiseguys.system.MovementSystem;
import io.whitegoldlabs.wiseguys.system.PickupSystem;
import io.whitegoldlabs.wiseguys.system.PlayerInputSystem;
import io.whitegoldlabs.wiseguys.system.RenderSystem;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class GameScreen implements Screen
{
	final WiseGuys game;

	SpriteBatch hudBatch;
	SpriteBatch debugBatch;
	
	Sprite coinSprite;
	
	OrthographicCamera camera;
	Texture spriteSheet;
	
	Engine engine;
	DebugRenderSystem debugRenderSystem;
	
	Entity player;
	PositionComponent playerPosition;
	VelocityComponent playerVelocity;
    AirborneStateComponent playerAirborneState;
    FacingDirectionStateComponent playerFacingState;
    InventoryComponent playerInventory;
	
	boolean debugMode = false;
	
	final float PLAYER_SPAWN_X = 16;
	final float PLAYER_SPAWN_Y = 32;
	
	short time = 400;
	float timer = 0;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GameScreen(final WiseGuys game)
	{
		this.game = game;
		
		hudBatch = new SpriteBatch();
		debugBatch = new SpriteBatch();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom -= 0.7f;
		
		spriteSheet = new Texture(Gdx.files.internal("sprites.png"));
		
		coinSprite = new Sprite(spriteSheet, 96, 0, 16, 16);
		coinSprite.setPosition(400, 662);
		coinSprite.setScale(2);
		
		Sprite playerStandingSprite = new Sprite(spriteSheet, 0, 0, 16, 16);
		Sprite playerJumpingSprite = new Sprite(spriteSheet, 64, 0, 16, 16);
		Array<Sprite> playerWalkingSprites = new Array<>();
		playerWalkingSprites.add(new Sprite(spriteSheet, 16, 0, 16, 16));
		playerWalkingSprites.add(new Sprite(spriteSheet, 32, 0, 16, 16));
		playerWalkingSprites.add(new Sprite(spriteSheet, 48, 0, 16, 16));
		
		Sprite playerHitboxSprite = new Sprite(spriteSheet, 144, 144, 16, 16);
		
		engine = new Engine();
		
		player = new Entity();
		player.add(new SpriteComponent(playerStandingSprite));
		player.add(new AirborneStateComponent(AirborneStateComponent.State.ON_GROUND));
		player.add(new FacingDirectionStateComponent(FacingDirectionStateComponent.State.FACING_RIGHT));
		player.add(new MovingStateComponent(MovingStateComponent.State.NOT_MOVING));
		player.add(new PositionComponent(PLAYER_SPAWN_X, PLAYER_SPAWN_Y));
		player.add(new VelocityComponent(0, 0));
		player.add(new AccelerationComponent(0, 0));
		player.add(new InventoryComponent(0, (byte)0, (byte)3));
		player.add(new AnimationComponent(playerStandingSprite, playerJumpingSprite, playerWalkingSprites));
		player.add(new HitboxComponent
		(
			PLAYER_SPAWN_X,
			PLAYER_SPAWN_Y,
			playerStandingSprite.getWidth(),
			playerStandingSprite.getHeight(),
			playerHitboxSprite
		));
		
		engine.addSystem(new MovementSystem());
		engine.addSystem(new GravitySystem());
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new RenderSystem(game.batch, camera));
		engine.addSystem(new PickupSystem(engine, player));
		engine.addSystem(new PlayerInputSystem(player));
		engine.addSystem(new AnimationSystem());
		
		debugRenderSystem = new DebugRenderSystem(game.batch, debugBatch, camera, game.font, player);
		engine.addSystem(debugRenderSystem);
		
		// Generate test world objects.
		Array<Entity> testWorldObjects = getTestWorldObjects();
		
		engine.addEntity(player);
		
		for(Entity testWorldObject : testWorldObjects)
		{
			engine.addEntity(testWorldObject);
		}
		
		debugRenderSystem.setProcessing(debugMode);
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
        
        playerPosition = Mappers.position.get(player);
		playerVelocity = Mappers.velocity.get(player);
	    playerAirborneState = Mappers.airborneState.get(player);
	    playerFacingState = Mappers.facingState.get(player);
	    playerInventory = Mappers.inventory.get(player);
        
	    // HUD
        hudBatch.begin();

        game.bigFont.draw(hudBatch, "JULES", 5, 710);
        game.bigFont.draw(hudBatch, String.format("%06d", playerInventory.score), 5, 680);
        
        coinSprite.draw(hudBatch);
        game.font.draw(hudBatch, "X", 425, 675);
        game.bigFont.draw(hudBatch, String.format("%02d", playerInventory.coins), 448, 680);
        
        game.bigFont.draw(hudBatch, "WORLD", 745, 710);
        game.bigFont.draw(hudBatch, "0-0", 777, 680);
        
        game.bigFont.draw(hudBatch, "TIME", 1138, 710);
        game.bigFont.draw(hudBatch, String.format("%03d", time), 1170, 680);

        hudBatch.end();
        
        // Timer
        timer += delta;
        if(timer > 1)
        {
        	time--;
        	timer = 0;
        }
        
        // Debug Mode
        if(Gdx.input.isKeyJustPressed(Keys.F4))
        {
        	debugMode = !debugMode;
        	debugRenderSystem.setProcessing(debugMode);
        }
        
        if(playerPosition.x <= 208)
        {
        	camera.position.set(208, 108, 0);
        }
        else
        {
        	camera.position.set(playerPosition.x, 108, 0);
        }
        
        engine.update(delta);
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
	
	private Array<Entity> getTestWorldObjects()
	{
		Array<Entity> entities = new Array<>();
		
		String[] csvLines = Gdx.files.internal("world1-1.csv").readString().split("\n");
		
		for(int y = csvLines.length - 1; y >= 0; y--)
		{
			String[] cells = csvLines[y].split(",");
			for(int x = 0; x < cells.length; x++)
			{
				// GOAL TOP
				if(cells[x].equals("59"))
				{
					Entity goalTop = new Entity();
					Sprite goalTopSprite = new Sprite(spriteSheet, 144, 80, 16, 16);
					Sprite goalTopHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					goalTop.add(new SpriteComponent(goalTopSprite));
					goalTop.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					goalTop.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						goalTopSprite.getWidth(),
						goalTopSprite.getHeight(),
						goalTopHitboxSprite
					));
					
					entities.add(goalTop);
				}
				// GOAL
				else if(cells[x].equals("69"))
				{
					Entity goal = new Entity();
					Sprite goalSprite = new Sprite(spriteSheet, 144, 96, 16, 16);
					goal.add(new SpriteComponent(goalSprite));
					goal.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					
					entities.add(goal);
				}
				// OPEN BATTLEMENTS
				else if(cells[x].equals("60"))
				{
					Entity openBattlements = new Entity();
					Sprite openBattlementsSprite = new Sprite(spriteSheet, 0, 96, 16, 16);
					Sprite openBattlementsHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					openBattlements.add(new SpriteComponent(openBattlementsSprite));
					openBattlements.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					openBattlements.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						openBattlementsSprite.getWidth(),
						openBattlementsSprite.getHeight(),
						openBattlementsHitboxSprite
					));
					
					entities.add(openBattlements);
				}
				// CASTLE WINDOW
				else if(cells[x].equals("61"))
				{
					Entity castleWindow = new Entity();
					Sprite castleWindowSprite = new Sprite(spriteSheet, 16, 96, 16, 16);
					Sprite castleWindowHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					castleWindow.add(new SpriteComponent(castleWindowSprite));
					castleWindow.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					castleWindow.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						castleWindowSprite.getWidth(),
						castleWindowSprite.getHeight(),
						castleWindowHitboxSprite
					));
					
					entities.add(castleWindow);
				}
				// CASTLE BRICKS
				else if(cells[x].equals("62"))
				{
					Entity castleBricks = new Entity();
					Sprite castleBricksSprite = new Sprite(spriteSheet, 32, 96, 16, 16);
					Sprite castleBricksHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					castleBricks.add(new SpriteComponent(castleBricksSprite));
					castleBricks.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					castleBricks.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						castleBricksSprite.getWidth(),
						castleBricksSprite.getHeight(),
						castleBricksHitboxSprite
					));
					
					entities.add(castleBricks);
				}
				// CASTLE WINDOW 2
				else if(cells[x].equals("63"))
				{
					Entity castleWindow2 = new Entity();
					Sprite castleWindow2Sprite = new Sprite(spriteSheet, 48, 96, 16, 16);
					Sprite castleWindow2HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					castleWindow2.add(new SpriteComponent(castleWindow2Sprite));
					castleWindow2.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					castleWindow2.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						castleWindow2Sprite.getWidth(),
						castleWindow2Sprite.getHeight(),
						castleWindow2HitboxSprite
					));
					
					entities.add(castleWindow2);
				}
				// ClOSED BATTLEMENTS
				else if(cells[x].equals("70"))
				{
					Entity closedBattlements = new Entity();
					Sprite closedBattlementsSprite = new Sprite(spriteSheet, 0, 112, 16, 16);
					Sprite closedBattlementsHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					closedBattlements.add(new SpriteComponent(closedBattlementsSprite));
					closedBattlements.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					closedBattlements.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						closedBattlementsSprite.getWidth(),
						closedBattlementsSprite.getHeight(),
						closedBattlementsHitboxSprite
					));
					
					entities.add(closedBattlements);
				}
				// CASTLE DOOR TOP
				else if(cells[x].equals("71"))
				{
					Entity castleDoorTop = new Entity();
					Sprite castleDoorTopSprite = new Sprite(spriteSheet, 16, 112, 16, 16);
					Sprite castleDoorTopHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					castleDoorTop.add(new SpriteComponent(castleDoorTopSprite));
					castleDoorTop.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					castleDoorTop.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						castleDoorTopSprite.getWidth(),
						castleDoorTopSprite.getHeight(),
						castleDoorTopHitboxSprite
					));
					
					entities.add(castleDoorTop);
				}
				// CASTLE DOOR
				else if(cells[x].equals("72"))
				{
					Entity castleDoor = new Entity();
					Sprite castleDoorSprite = new Sprite(spriteSheet, 32, 112, 16, 16);
					Sprite castleDoorHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					castleDoor.add(new SpriteComponent(castleDoorSprite));
					castleDoor.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					castleDoor.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						castleDoorSprite.getWidth(),
						castleDoorSprite.getHeight(),
						castleDoorHitboxSprite
					));
					
					entities.add(castleDoor);
				}
				// UP PIPE 1
				else if(cells[x].equals("75"))
				{
					Entity upPipe1 = new Entity();
					Sprite UpPipe1Sprite = new Sprite(spriteSheet, 80, 112, 16, 16);
					Sprite UpPipe1HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					upPipe1.add(new SpriteComponent(UpPipe1Sprite));
					upPipe1.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					upPipe1.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						UpPipe1Sprite.getWidth(),
						UpPipe1Sprite.getHeight(),
						UpPipe1HitboxSprite
					));
					
					entities.add(upPipe1);
				}
				// UP PIPE 2
				else if(cells[x].equals("76"))
				{
					Entity upPipe2 = new Entity();
					Sprite UpPipe2Sprite = new Sprite(spriteSheet, 96, 112, 16, 16);
					Sprite UpPipe2HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					upPipe2.add(new SpriteComponent(UpPipe2Sprite));
					upPipe2.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					upPipe2.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						UpPipe2Sprite.getWidth(),
						UpPipe2Sprite.getHeight(),
						UpPipe2HitboxSprite
					));
					
					entities.add(upPipe2);
				}
				// UP PIPE 3
				else if(cells[x].equals("85"))
				{
					Entity upPipe3 = new Entity();
					Sprite UpPipe3Sprite = new Sprite(spriteSheet, 80, 128, 16, 16);
					Sprite UpPipe3HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					upPipe3.add(new SpriteComponent(UpPipe3Sprite));
					upPipe3.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					upPipe3.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						UpPipe3Sprite.getWidth(),
						UpPipe3Sprite.getHeight(),
						UpPipe3HitboxSprite
					));
					
					entities.add(upPipe3);
				}
				// UP PIPE 4
				else if(cells[x].equals("86"))
				{
					Entity upPipe4 = new Entity();
					Sprite UpPipe4Sprite = new Sprite(spriteSheet, 96, 128, 16, 16);
					Sprite UpPipe4HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					upPipe4.add(new SpriteComponent(UpPipe4Sprite));
					upPipe4.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					upPipe4.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						UpPipe4Sprite.getWidth(),
						UpPipe4Sprite.getHeight(),
						UpPipe4HitboxSprite
					));
					
					entities.add(upPipe4);
				}
				// LEFT PIPE 1
				else if(cells[x].equals("77"))
				{
					Entity leftPipe1 = new Entity();
					Sprite leftPipe1Sprite = new Sprite(spriteSheet, 112, 112, 16, 16);
					Sprite leftPipe1HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					leftPipe1.add(new SpriteComponent(leftPipe1Sprite));
					leftPipe1.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					leftPipe1.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						leftPipe1Sprite.getWidth(),
						leftPipe1Sprite.getHeight(),
						leftPipe1HitboxSprite
					));
					
					entities.add(leftPipe1);
				}
				// LEFT PIPE 2
				else if(cells[x].equals("78"))
				{
					Entity leftPipe2 = new Entity();
					Sprite leftPipe2Sprite = new Sprite(spriteSheet, 128, 112, 16, 16);
					Sprite leftPipe2HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					leftPipe2.add(new SpriteComponent(leftPipe2Sprite));
					leftPipe2.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					leftPipe2.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						leftPipe2Sprite.getWidth(),
						leftPipe2Sprite.getHeight(),
						leftPipe2HitboxSprite
					));
					
					entities.add(leftPipe2);
				}
				// LEFT PIPE 3
				else if(cells[x].equals("79"))
				{
					Entity leftPipe3 = new Entity();
					Sprite leftPipe3Sprite = new Sprite(spriteSheet, 144, 112, 16, 16);
					Sprite leftPipe3HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					leftPipe3.add(new SpriteComponent(leftPipe3Sprite));
					leftPipe3.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					leftPipe3.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						leftPipe3Sprite.getWidth(),
						leftPipe3Sprite.getHeight(),
						leftPipe3HitboxSprite
					));
					
					entities.add(leftPipe3);
				}
				// LEFT PIPE 4
				else if(cells[x].equals("87"))
				{
					Entity leftPipe4 = new Entity();
					Sprite leftPipe4Sprite = new Sprite(spriteSheet, 112, 128, 16, 16);
					Sprite leftPipe4HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					leftPipe4.add(new SpriteComponent(leftPipe4Sprite));
					leftPipe4.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					leftPipe4.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						leftPipe4Sprite.getWidth(),
						leftPipe4Sprite.getHeight(),
						leftPipe4HitboxSprite
					));
					
					entities.add(leftPipe4);
				}
				// LEFT PIPE 5
				else if(cells[x].equals("88"))
				{
					Entity leftPipe5 = new Entity();
					Sprite leftPipe5Sprite = new Sprite(spriteSheet, 128, 128, 16, 16);
					Sprite leftPipe5HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					leftPipe5.add(new SpriteComponent(leftPipe5Sprite));
					leftPipe5.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					leftPipe5.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						leftPipe5Sprite.getWidth(),
						leftPipe5Sprite.getHeight(),
						leftPipe5HitboxSprite
					));
					
					entities.add(leftPipe5);
				}
				// LEFT PIPE 6
				else if(cells[x].equals("89"))
				{
					Entity leftPipe6 = new Entity();
					Sprite leftPipe6Sprite = new Sprite(spriteSheet, 144, 128, 16, 16);
					Sprite leftPipe6HitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					leftPipe6.add(new SpriteComponent(leftPipe6Sprite));
					leftPipe6.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					leftPipe6.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						leftPipe6Sprite.getWidth(),
						leftPipe6Sprite.getHeight(),
						leftPipe6HitboxSprite
					));
					
					entities.add(leftPipe6);
				}
				// BLOCK
				else if(cells[x].equals("80"))
				{
					Entity block = new Entity();
					Sprite blockSprite = new Sprite(spriteSheet, 0, 128, 16, 16);
					Sprite blockHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					block.add(new SpriteComponent(blockSprite));
					block.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					block.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						blockSprite.getWidth(),
						blockSprite.getHeight(),
						blockHitboxSprite
					));
					
					entities.add(block);
				}
				// GROUND
				else if(cells[x].equals("81"))
				{
					Entity ground = new Entity();
					Sprite groundSprite = new Sprite(spriteSheet, 16, 128, 16, 16);
					Sprite groundHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					ground.add(new SpriteComponent(groundSprite));
					ground.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					ground.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						groundSprite.getWidth(),
						groundSprite.getHeight(),
						groundHitboxSprite
					));
					
					entities.add(ground);
				}
				// BOX
				else if(cells[x].equals("82"))
				{
					Entity box = new Entity();
					Sprite boxSprite = new Sprite(spriteSheet, 32, 128, 16, 16);
					Sprite boxHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					box.add(new SpriteComponent(boxSprite));
					box.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					box.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						boxSprite.getWidth(),
						boxSprite.getHeight(),
						boxHitboxSprite
					));
					
					entities.add(box);
				}
				// BRICKS TOP
				else if(cells[x].equals("83"))
				{
					Entity bricksTop = new Entity();
					Sprite bricksTopSprite = new Sprite(spriteSheet, 48, 128, 16, 16);
					Sprite bricksTopHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					bricksTop.add(new SpriteComponent(bricksTopSprite));
					bricksTop.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					bricksTop.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						bricksTopSprite.getWidth(),
						bricksTopSprite.getHeight(),
						bricksTopHitboxSprite
					));
					
					entities.add(bricksTop);
				}
				// BRICKS
				else if(cells[x].equals("84"))
				{
					Entity bricks = new Entity();
					Sprite bricksSprite = new Sprite(spriteSheet, 64, 128, 16, 16);
					Sprite bricksHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					bricks.add(new SpriteComponent(bricksSprite));
					bricks.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					bricks.add(new HitboxComponent
					(
						x*16,
						(csvLines.length-1-y)*16,
						bricksSprite.getWidth(),
						bricksSprite.getHeight(),
						bricksHitboxSprite
					));
					
					entities.add(bricks);
				}
			}
		}
		
		return entities;
	}
}
