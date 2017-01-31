Jules_death_behavior = {}

function Jules_death_behavior.execute(game, startingY)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")

  local julesDeathSfx = game.assets.manager:get(assetFiles.sfxJulesDeath)

  local playerBehaviorComponent = mappers.behavior:get(game.player)
  local playerPositionComponent = mappers.position:get(game.player)
  local playerVelocityComponent = mappers.velocity:get(game.player)
  
  if playerBehaviorComponent.behaviorState == "DYING" then
    if playerBehaviorComponent.behaviorTime == 0 then
      julesDeathSfx:play()
      game.eventProcessing = true
    end
    
    if playerBehaviorComponent.behaviorTime >= 0.5 then
      playerBehaviorComponent.behaviorState = "GOING_UP"
    end
  elseif playerBehaviorComponent.behaviorState == "GOING_UP" then
    if playerPositionComponent.y >= startingY + 80 then
      playerBehaviorComponent.behaviorState = "FALLING_BACK_DOWN"
      playerBehaviorComponent.behaviorTime = 0
    else
      playerPositionComponent.y = playerPositionComponent.y + 4
    end
  elseif playerBehaviorComponent.behaviorState == "FALLING_BACK_DOWN" then
    if playerBehaviorComponent.behaviorTime >= 0.5 then
      playerPositionComponent.y = playerPositionComponent.y - 4
    end
  end
end

return Jules_death_behavior
