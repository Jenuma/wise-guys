Box_behavior = {}

function Box_behavior.execute(box, startingY)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")

  local boxBehaviorComponent = mappers.behavior:get(box)
  local boxPositionComponent = mappers.position:get(box)
  
  -----------------
  -- Bouncing Up --
  -----------------
  if boxBehaviorComponent.behaviorState == "BOUNCING_UP" then
    if math.floor(boxPositionComponent.y) == startingY + 4 then
      boxBehaviorComponent.behaviorState = "BOUNCING_DOWN"
    else
      boxPositionComponent.y = boxPositionComponent.y + 0.8
    end
    
  -------------------
  -- Bouncing Down --
  -------------------
  elseif boxBehaviorComponent.behaviorState == "BOUNCING_DOWN" then
    if math.floor(boxPositionComponent.y) < startingY - 2 then
      boxBehaviorComponent.behaviorState = "BOUNCING_BACK_UP"
    else
      boxPositionComponent.y = boxPositionComponent.y - 0.8
    end
    
  ----------------------
  -- Bouncing Back Up --
  ----------------------
  elseif boxBehaviorComponent.behaviorState == "BOUNCING_BACK_UP" then
    if math.floor(boxPositionComponent.y) == startingY then
      local behaviors = luajava.bindClass("io.whitegoldlabs.wiseguys.component.BehaviorComponent")
      
      boxPositionComponent.y = startingY
      box:remove(behaviors)
    else
      boxPositionComponent.y = boxPositionComponent.y + 0.8
    end
  end
end

return Box_behavior
