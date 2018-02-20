dofile("/Users/ryuu/git/tutorials/lua/2-table.lua")
dofile("/Users/ryuu/git/tutorials/lua/3-function.lua")

local f = loadfile("/Users/ryuu/git/tutorials/lua/3-function.lua")
f()

i = 1
local fs = assert(loadstring("i = i + 1"))()
print(i)
print(i)

-- print "enter a number"
-- n=io.read("*number")
-- if not n then 
--     error("invalid input")
-- end
