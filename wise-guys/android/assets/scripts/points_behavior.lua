Points_behavior = {}

function Points_behavior.execute(game, amount, digit1, digit2, digit3, x, y)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local pointsBehavior = mappers.behavior:get(digit1)
  
  if pointsBehavior.behaviorState == "NOT_INITIALIZED" then
    local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
    local spriteSheet = game.assets.manager:get(assetFiles.spriteSheet)
    local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
  
    local digit1Sprite
    
    if amount == 100 then
      digit1Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 4, 144, 4, 8)
    elseif amount == 200 then
      digit1Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 8, 144, 4, 8)
    elseif amount == 400 then
      digit1Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 12, 144, 4, 8)
    elseif amount == 500 then
      digit1Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 0, 152, 4, 8)
    elseif amount == 800 then
      digit1Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 4, 152, 4, 8)
    else
      digit1Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 0, 144, 4, 8)
    end
    
    local digit2Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 0, 144, 4, 8)
    local digit3Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 0, 144, 4, 8)
    
    local digit1PositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", x, y)
    local digit2PositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", x + 5, y)
    local digit3PositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", x + 10, y)
    
    local pointsTypeComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.TypeComponent", types.Type.PRIORITY)
    local pointsStateComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.StateComponent")
    local pointsPhaseComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PhaseComponent")

    local digit1SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", digit1Sprite)
    local digit2SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", digit2Sprite)
    local digit3SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", digit3Sprite)
    
    digit1:add(pointsTypeComponent)
    digit1:add(pointsStateComponent)
    digit1:add(digit1PositionComponent)
    digit1:add(pointsPhaseComponent)
    digit1:add(digit1SpriteComponent)
    digit1:add(digit1SpriteComponent)
    
    digit2:add(pointsTypeComponent)
    digit2:add(pointsStateComponent)
    digit2:add(digit2PositionComponent)
    digit2:add(pointsPhaseComponent)
    digit2:add(digit2SpriteComponent)
    digit2:add(digit2SpriteComponent)
    
    digit3:add(pointsTypeComponent)
    digit3:add(pointsStateComponent)
    digit3:add(digit3PositionComponent)
    digit3:add(pointsPhaseComponent)
    digit3:add(digit3SpriteComponent)
    digit3:add(digit3SpriteComponent)
    
    pointsBehavior.behaviorState = "INITIALIZED"
  elseif pointsBehavior.behaviorState == "INITIALIZED" then
    local digit1Position = mappers.position:get(digit1)
    local digit2Position = mappers.position:get(digit2)
    local digit3Position = mappers.position:get(digit3)
    
    digit1Position.y = digit1Position.y + 0.1
    digit2Position.y = digit2Position.y + 0.1
    digit3Position.y = digit3Position.y + 0.1
    
    if pointsBehavior.behaviorTime > 3 then
      local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
    
      local pointsState = mappers.state:get(digit1)
      
      pointsState.enabledState = states.EnabledState.DISABLED
    end
  end
end

return Points_behavior
