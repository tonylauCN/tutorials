require("lua.Set")
t = {}
print(getmetatable(t))
print(getmetatable("Hi"))
print(getmetatable(10))

-- do set test
local arr1 = {"a", "b", "c", 1, 2, 3}
local arr2 = {"a", 1}
local s1 = {}
local s2 = {}
s1 = Set.new(arr1)
s2 = Set.new(arr2)

print("Set1 is: \t" .. Set.tostring(s1))
print("Set2 is: \t" .. Set.tostring(s2))
print("Set.union is: \t\t\t\t\t" .. Set.tostring(Set.union(s1, s2)))
print("Set.union using metatable is: \t" .. Set.tostring(s1 + s2))

print("Set.intersection is \t\t\t\t\t" .. Set.tostring(Set.intersection(s1, s2)))
print("Set.intersection using metadable is: \t" .. Set.tostring(s1 * s2))

-- default value sample
Window = {}
Window.prototype = {x = 10, y = 10, width = 100, height = 100}
Window.mt = {}
Window.mt.__index = function(table, key)
    return Window.prototype[key]
end
function Window.new(o)
    setmetatable(o, Window.mt)
    return o
end

w = Window.new {x = 10, y = 20}
print(w.width .. ":" .. w.height)

-- readonly table
function readonly(o)
    local proxy = {}
    local mt = {
        __index = o,
        __newindex = function(t, k, v)
            error("attempt to update a read-only table", 2)
        end
    }
    setmetatable(proxy, mt)
    return proxy
end

days = readonly{"Sunday", "Monday", "Tuesday", "Wedensday", "Thursday", "Friday", "Saturday"}
print(days[1])
days[2] = "demo"


