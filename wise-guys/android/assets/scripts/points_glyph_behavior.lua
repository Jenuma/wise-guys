Points_glyph_behavior = {}

function Points_glyph_behavior.execute(game, x, y)
  local points = luajava.newInstance("com.badlogic.gdx.graphics.g2d.GlyphLayout", game.font, "200")
  
  game.batch:begin()
  game.font:draw(game.batch, points, x, y)
end

return Points_glyph_behavior
