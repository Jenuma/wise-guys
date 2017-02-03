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
    local bricksPosition = mappers.position:get(bricks)
  
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
          local spriteSheet = game.assets.manager:get(assetFiles.spriteSheet)
          local brickSmashSfx = game.assets.manager:get(assetFiles.sfxBrickSmash)
          local bricksStateComponent = mappers.state:get(bricks)
          
          brickSmashSfx:play()
          
          local q1Piece = luajava.newInstance("com.badlogic.ashley.core.Entity")
          local q2Piece = luajava.newInstance("com.badlogic.ashley.core.Entity")
          local q3Piece = luajava.newInstance("com.badlogic.ashley.core.Entity")
          local q4Piece = luajava.newInstance("com.badlogic.ashley.core.Entity")
          
          local pieceType = luajava.newInstance("io.whitegoldlabs.wiseguys.component.TypeComponent", types.Type.PRIORITY)
          local pieceAccel = luajava.newInstance("io.whitegoldlabs.wiseguys.component.AccelerationComponent", 0, 3)
          local pieceState = luajava.newInstance("io.whitegoldlabs.wiseguys.component.StateComponent")
          local piecePhase = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PhaseComponent")
          
          pieceState.airborneState = states.AirborneState.FALLING
          
          local q1Position = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", bricksPosition.x + 8, bricksPosition.y + 16)
          local q2Position = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", bricksPosition.x, bricksPosition.y + 16)
          local q3Position = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", bricksPosition.x, bricksPosition.y + 8)
          local q4Position = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", bricksPosition.x + 8, bricksPosition.y + 8)
          
          local q1Hitbox = luajava.newInstance("io.whitegoldlabs.wiseguys.component.HitboxComponent", q1Position.x, q1Position.y, 8, 8)
          local q2Hitbox = luajava.newInstance("io.whitegoldlabs.wiseguys.component.HitboxComponent", q2Position.x, q2Position.y, 8, 8)
          local q3Hitbox = luajava.newInstance("io.whitegoldlabs.wiseguys.component.HitboxComponent", q3Position.x, q3Position.y, 8, 8)
          local q4Hitbox = luajava.newInstance("io.whitegoldlabs.wiseguys.component.HitboxComponent", q4Position.x, q4Position.y, 8, 8)
          
          local rightVelocity = luajava.newInstance("io.whitegoldlabs.wiseguys.component.VelocityComponent", 1, 1)
          local leftVelocity = luajava.newInstance("io.whitegoldlabs.wiseguys.component.VelocityComponent", -1, 1)
          
          local q1Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 32, 128, 8, 8)
          local q2Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 32, 128, 8, 8)
          local q3Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 32, 128, 8, 8)
          local q4Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 32, 128, 8, 8)
          
          q1Sprite:setPosition(q1Position.x, q1Position.y)
          q2Sprite:setPosition(q2Position.x, q2Position.y)
          q3Sprite:setPosition(q3Position.x, q3Position.y)
          q4Sprite:setPosition(q4Position.x, q4Position.y)
          
          local q1SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", q1Sprite)
          local q2SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", q2Sprite)
          local q3SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", q3Sprite)
          local q4SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", q4Sprite)
          
          q1Piece:add(pieceType)
          q1Piece:add(q1Position)
          q1Piece:add(q1SpriteComponent)
          q1Piece:add(rightVelocity)
          q1Piece:add(pieceAccel)
          q1Piece:add(pieceState)
          q1Piece:add(q1Hitbox)
          q1Piece:add(piecePhase)
          
          q2Piece:add(pieceType)
          q2Piece:add(q2Position)
          q2Piece:add(q2SpriteComponent)
          q2Piece:add(leftVelocity)
          q2Piece:add(pieceAccel)
          q2Piece:add(pieceState)
          q2Piece:add(q2Hitbox)
          q2Piece:add(piecePhase)
          
          q3Piece:add(pieceType)
          q3Piece:add(q3Position)
          q3Piece:add(q3SpriteComponent)
          q3Piece:add(leftVelocity)
          q3Piece:add(pieceAccel)
          q3Piece:add(pieceState)
          q3Piece:add(q3Hitbox)
          q3Piece:add(piecePhase)
          
          q4Piece:add(pieceType)
          q4Piece:add(q4Position)
          q4Piece:add(q4SpriteComponent)
          q4Piece:add(rightVelocity)
          q4Piece:add(pieceAccel)
          q4Piece:add(pieceState)
          q4Piece:add(q4Hitbox)
          q4Piece:add(piecePhase)
          
          game.engine:addEntity(q1Piece)
          game.engine:addEntity(q2Piece)
          game.engine:addEntity(q3Piece)
          game.engine:addEntity(q4Piece)
          
          bricksStateComponent.enabledState = states.EnabledState.DISABLED
        end
      end
    end
  end
end

return Bricks
