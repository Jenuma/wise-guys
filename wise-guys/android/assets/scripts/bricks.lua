Bricks = {}

function Bricks.execute(bricks, game)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
  local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
  
  local collisionComponent = mappers.collision:get(bricks)
  local collidingEntity = collisionComponent.collidingWith
  local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
  
  ---------------------------
  -- Collision With Player --
  ---------------------------
  if collidingEntityTypeComponent.type == types.Type.PLAYER then
    local playerComponent = mappers.player:get(collidingEntity)
    local playerHitboxComponent = mappers.hitbox:get(collidingEntity)
    local bricksHitboxComponent = mappers.hitbox:get(bricks)
  
    --------------------------------
    -- Get Intersection Rectangle --
    --------------------------------
    local intersectX = math.max(playerHitboxComponent.hitbox.x, bricksHitboxComponent.hitbox.x)
    local intersectY = math.max(playerHitboxComponent.hitbox.y, bricksHitboxComponent.hitbox.y)
    local intersectWidth = math.min(playerHitboxComponent.hitbox.x + playerHitboxComponent.hitbox.width, bricksHitboxComponent.hitbox.x + bricksHitboxComponent.hitbox.width) - intersectX
    local intersectHeight = math.min(playerHitboxComponent.hitbox.y + playerHitboxComponent.hitbox.height, bricksHitboxComponent.hitbox.y + bricksHitboxComponent.hitbox.height) - intersectY
    
    if intersectWidth > intersectHeight then
      if playerHitboxComponent.hitbox.y < bricksHitboxComponent.hitbox.y then
        local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
        
        -----------------
        -- Bricks Bump --
        -----------------
        if playerComponent.playerState == playerStates.PlayerState.NORMAL then
          if not mappers.behavior:has(bricks) then
            local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
            local bumpSfx = game.assets.manager:get(assetFiles.sfxBump)
          
            local bricksPosition = mappers.position:get(bricks)
            
            bumpSfx:play()
            
            local scriptArgs = arrayInstantiator:getNewArray()
            scriptArgs:add(bricks)
            scriptArgs:add(bricksPosition.y)
            
            local boxBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\bounce_behavior.lua", scriptArgs)
            boxBehaviorComponent.behaviorState = "BOUNCING_UP"
            game.scriptManager:loadScript("scripts\\bounce_behavior.lua")
          
            bricks:add(boxBehaviorComponent)
          end
          
        ------------------
        -- Bricks Break --
        ------------------
        else
          local brickSmashSfx = game.assets.manager:get(assetFiles.sfxBrickSmash)
          local bricksStateComponent = mappers.state:get(bricks)
          
          brickSmashSfx:play()
          bricksStateComponent.enabledState = states.EnabledState.DISABLED
        end
      end
    end
  end
end

return Bricks
