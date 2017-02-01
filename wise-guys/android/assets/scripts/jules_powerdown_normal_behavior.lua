Jules_powerdown_normal_behavior = {}

function Jules_powerdown_normal_behavior.execute(game)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
  local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  local animations = luajava.bindClass("com.badlogic.gdx.graphics.g2d.Animation")
  
  local playerBehaviorComponent = mappers.behavior:get(game.player)
  local playerComponent = mappers.player:get(game.player)
  local playerStateComponent = mappers.state:get(game.player)
  local playerAnimationComponent = mappers.animation:get(game.player)
  
  local playerPowerdownAnimation = playerAnimationComponent.animations:get("POWERUP")
  playerPowerdownAnimation:setPlayMode(animations.PlayMode.REVERSED)
  
  if playerBehaviorComponent.behaviorState == "TOUCHED" then
    game.eventProcessing = true
    
    playerBehaviorComponent.behaviorState = "POWERDOWN_ANIMATION"
    playerStateComponent.time = 0
  elseif playerBehaviorComponent.behaviorState == "POWERDOWN_ANIMATION" then
    local playerSpriteComponent = mappers.sprite:get(game.player)
    
    playerSpriteComponent.sprite = playerPowerdownAnimation:getKeyFrame(playerStateComponent.time, false)
    
    if playerStateComponent.directionState == states.DirectionState.LEFT then
      playerSpriteComponent.sprite:setFlip(true, false)
    elseif playerStateComponent.directionState == states.DirectionState.LEFT then
      playerSpriteComponent.sprite:setFlip(false, false)
    end
    
    if playerPowerdownAnimation:isAnimationFinished(playerStateComponent.time) then
      local behaviors = luajava.bindClass("io.whitegoldlabs.wiseguys.component.BehaviorComponent")
    
      game:powerdownNormalJules()
      game.player:remove(behaviors)
      game.eventProcessing = false
      
      playerComponent.damaged = true
    end
  end
end

return Jules_powerdown_normal_behavior
