Warp_behavior = {}

function Warp_behavior.execute(game, triggerKey, destination, x, y)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")

  local playerBehaviorComponent = mappers.behavior:get(game.player)
  local playerPositionComponent = mappers.position:get(game.player)
  
  if playerBehaviorComponent.behaviorState == "WARP_STARTED" then
    local warpSfx = game.assets.manager:get(assetFiles.sfxPipe)
  
    warpSfx:play()
    game.eventProcessing = true
    
    playerBehaviorComponent.behaviorState = "WARPING"
  elseif playerBehaviorComponent.behaviorState == "WARPING" then
    if playerBehaviorComponent.behaviorTime > 1 then
      local behaviors = luajava.bindClass("io.whitegoldlabs.wiseguys.component.BehaviorComponent")
    
      game:prepareNextGameScreen(destination, x, y)
      game.player:remove(behaviors)
      game.eventProcessing = false;
    else
      local gdx = luajava.bindClass("com.badlogic.gdx.Gdx")
      local input = luajava.bindClass("com.badlogic.gdx.Input")
    
      if triggerKey == input.Keys.DOWN then
        playerPositionComponent.y = playerPositionComponent.y - 1
      elseif triggerKey == input.Keys.RIGHT then
        playerPositionComponent.x = playerPositionComponent.x + 1
      end
    end
  end
end

return Warp_behavior
