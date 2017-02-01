Jules_connolo_behavior = {}

function Jules_connolo_behavior.execute(game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
	local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
	
	local playerBehaviorComponent = mappers.behavior:get(game.player)
	local playerComponent = mappers.player:get(game.player)
	local playerStateComponent = mappers.state:get(game.player)
	local playerAnimationComponent = mappers.animation:get(game.player)
	
	if playerBehaviorComponent.behaviorState == "TOUCHED" then
	  game.eventProcessing = true
	  
	  playerBehaviorComponent.behaviorState = "POWERUP_ANIMATION"
	  playerStateComponent.time = 0
	elseif playerBehaviorComponent.behaviorState == "POWERUP_ANIMATION" then
	  local playerSpriteComponent = mappers.sprite:get(game.player)
	  local playerPowerupAnimation = playerAnimationComponent.animations:get("POWERUP")
      
    playerSpriteComponent.sprite = playerPowerupAnimation:getKeyFrame(playerStateComponent.time, false)
    
    if playerStateComponent.directionState == states.DirectionState.LEFT then
      playerSpriteComponent.sprite:setFlip(true, false)
    elseif playerStateComponent.directionState == states.DirectionState.LEFT then
      playerSpriteComponent.sprite:setFlip(false, false)
    end
    
    if playerPowerupAnimation:isAnimationFinished(playerStateComponent.time) then
      local behaviors = luajava.bindClass("io.whitegoldlabs.wiseguys.component.BehaviorComponent")
    
      game:powerupSuperJules()
      game.player:remove(behaviors)
      game.eventProcessing = false
    end
	end
end

return Jules_connolo_behavior
