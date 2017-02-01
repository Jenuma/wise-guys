Jules_death_behavior = {}

function Jules_death_behavior.execute(game, startingY)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
  local animations = luajava.bindClass("io.whitegoldlabs.wiseguys.component.AnimationComponent")
  local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
  local renderSystem = luajava.bindClass("io.whitegoldlabs.wiseguys.system.RenderSystem")
      
  local spriteSheet = game.assets.manager:get(assetFiles.spriteSheet)
  local julesDeathSfx = game.assets.manager:get(assetFiles.sfxJulesDeath)

  local playerBehaviorComponent = mappers.behavior:get(game.player)
  local playerVelocityComponent = mappers.velocity:get(game.player)
  local playerAccelerationComponent = mappers.acceleration:get(game.player)
  local playerTypeComponent = mappers.type:get(game.player)
  local playerPhaseComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PhaseComponent")
  
  local engineRenderSystem = game.engine:getSystem(renderSystem)
  
  if playerBehaviorComponent.behaviorState == "DYING" then
    if playerBehaviorComponent.behaviorTime == 0 then
      local playerSpriteComponent = mappers.sprite:get(game.player)
      
      game.player:add(playerPhaseComponent)
      game.player:remove(animations)
      playerSpriteComponent.sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 80, 0, 16, 16)
      julesDeathSfx:play()
      game.eventProcessing = true
      
      playerVelocityComponent.x = 0;
      playerVelocityComponent.y = 0;
      playerAccelerationComponent.x = 0;
      playerAccelerationComponent.y = 0;
    end
    
    if playerBehaviorComponent.behaviorTime >= 0.5 then
      local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
      local playerStateComponent = mappers.state:get(game.player)
    
      game.playerEventMovementAllowed = true;
      playerVelocityComponent.y = 530;
      
      playerStateComponent.airborneState = states.AirborneState.FALLING
      playerTypeComponent.type = types.Type.PRIORITY
      engineRenderSystem:forceSort()
      playerBehaviorComponent.behaviorState = "DYING_ANIMATION"
    end
  elseif playerBehaviorComponent.behaviorState == "DYING_ANIMATION" then
    if playerBehaviorComponent.behaviorTime >= 2.5 then
      local worlds = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Worlds")
      local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
      local behaviors = luajava.bindClass("io.whitegoldlabs.wiseguys.component.BehaviorComponent")
      local phases = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PhaseComponent")
    
      local player = mappers.player:get(game.player)
    
      player.lives = player.lives - 1
      game:powerdownNormalJules()
      
      playerTypeComponent.type = types.Type.PLAYER
      engineRenderSystem:forceSort()
      game.player:remove(behaviors)
      game.player:remove(phases)
      
      worlds:unloadWorldEntities()
      
      if player.lives > 0 then
        newScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.WorldIntroScreen", game, game.currentWorld)
      else
        newScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameOverScreen", game)
      end
      
      game.currentScreen:dispose()
      game.currentScreen = newScreen
      game:setScreen(newScreen)
      
      game.eventProcessing = false;
      game.playerEventMovementAllowed = false;
    end
  end
end

return Jules_death_behavior
