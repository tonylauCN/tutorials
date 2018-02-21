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

