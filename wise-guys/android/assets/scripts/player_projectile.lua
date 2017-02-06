Player_projectile = {}

function Player_projectile.execute(projectile, game)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
  local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  
  local collisionComponent = mappers.collision:get(projectile)
  local collidingEntity = collisionComponent.collidingWith
  local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
  local collidingEntityHitbox = mappers.hitbox:get(collidingEntity)
  
  local projectileHitbox = mappers.hitbox:get(projectile)
  local projectileStateComponent = mappers.state:get(projectile)
  
  -----------------------------
  -- Collision with Obstacle --
  -----------------------------
  if collidingEntityTypeComponent.type == types.Type.OBSTACLE then
    local projectilePosition = mappers.position:get(projectile)
    local projectileVelocity = mappers.velocity:get(projectile)
  
    ---------------------
    -- Vertical Bounce --
    ---------------------
    if projectileHitbox.hitbox.x + (projectileHitbox.hitbox.width / 2) >= collidingEntityHitbox.hitbox.x and projectileHitbox.hitbox.x <= collidingEntityHitbox.hitbox.x + collidingEntityHitbox.hitbox.width then
      if projectileVelocity.y > 0 then
        projectilePosition.y = collidingEntityHitbox.hitbox.y - projectileHitbox.hitbox.height
      else
        projectilePosition.y = collidingEntityHitbox.hitbox.y + collidingEntityHitbox.hitbox.height
      end
      
      projectileHitbox.hitbox.y = projectilePosition.y
      projectileVelocity.y = 0 - projectileVelocity.y
      
    -----------------------
    -- Horizontal Bounce --
    -----------------------
    elseif projectileHitbox.hitbox.y + (projectileHitbox.hitbox.height / 2) >= collidingEntityHitbox.hitbox.y and projectileHitbox.hitbox.y <= collidingEntityHitbox.hitbox.y + collidingEntityHitbox.hitbox.height then
      if projectileVelocity.x > 0 then
        projectilePosition.x = collidingEntityHitbox.hitbox.x - projectileHitbox.hitbox.width
      else
        projectilePosition.x = collidingEntityHitbox.hitbox.x + collidingEntityHitbox.hitbox.width
      end
      
      projectileHitbox.hitbox.x = projectilePosition.x
      projectileVelocity.x = 0 - projectileVelocity.x
      
    ------------
    -- Corner --
    ------------
    else
      projectileStateComponent.enabledState = states.EnabledState.DISABLED
    end
    
  --------------------------
  -- Collision with Enemy --
  --------------------------
  elseif collidingEntityTypeComponent.type == types.Type.ENEMY then
    local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
    local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
    
    local collidingEntityStateComponent = mappers.state:get(collidingEntity)
    local collidingEntityPosition = mappers.position:get(collidingEntity)
    local kickSfx = game.assets.manager:get(assetFiles.sfxKick)
    
    kickSfx:play()
    projectileStateComponent.enabledState = states.EnabledState.DISABLED
    collidingEntityStateComponent.enabledState = states.EnabledState.DISABLED
    
    local digit1Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    local digit2Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    local digit3Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    
    local scriptArgs = arrayInstantiator:getNewArray()
    scriptArgs:add(game)
    scriptArgs:add(200)
    scriptArgs:add(digit1Entity)
    scriptArgs:add(digit2Entity)
    scriptArgs:add(digit3Entity)
    scriptArgs:add(collidingEntityPosition.x)
    scriptArgs:add(collidingEntityPosition.y)
    
    local pointsBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\points_behavior.lua", scriptArgs)
    pointsBehaviorComponent.behaviorState = "NOT_INITIALIZED"
    game.scriptManager:loadScript("scripts\\points_behavior.lua")
    
    digit1Entity:add(pointsBehaviorComponent)
    digit2Entity:add(pointsBehaviorComponent)
    digit3Entity:add(pointsBehaviorComponent)
    
    game.engine:addEntity(digit1Entity)
    game.engine:addEntity(digit2Entity)
    game.engine:addEntity(digit3Entity)
  end
end

return Player_projectile
