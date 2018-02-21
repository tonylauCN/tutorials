--
-- sample for Set
--
Set = {}
local mt = {} -- metatatable
function Set.new(arr)
    local set = {}
    setmetatable(set, mt)
    for _, v in ipairs(arr) do
        set[v] = true
    end
    return set
end

function Set.union(set1, set2)
    -- metatable validation
    if (getmetatable(set1) ~= mt or getmetatable(set2) ~= mt) then
        error("attempt to 'add' a set with non-set value", 2)
    end
    local set = Set.new({})
    for k in pairs(set1) do
        set[k] = true
    end
    for k in pairs(set2) do
        set[k] = true
    end
    return set
end

function Set.intersection(set1, set2)
    -- metatable validation
    if (getmetatable(set1) ~= mt or getmetatable(set2) ~= mt) then
        error("attempt to 'mul' a set with non-set value", 2)
    end
    local set = Set.new({})
    for k in pairs(set1) do
        set[k] = set2[k]
    end
    return set
end

function Set.tostring(set)
    local l = {}
    for k in pairs(set) do
        l[#l + 1] = k
    end
    return "{" .. table.concat(l, ", ") .. "}"
end

function Set.print(set)
    print(Set.tostring(set))
end

print("Set\t[Loaded]")

mt.__add = Set.union -- "+" for union
mt.__mul = Set.intersection -- "*" for intersection
-- <=
mt.__le = function(a, b)
    for k in pairs(a) do
        if not b[k] then
            return false
        end
    end
    return true
end
mt.__lt = function(a, b)
    return a <= b and not (b <= a)
end
mt.__eq = function(a, b)
    return a <= b and b <= a
end
--[[ 元表算数及关系类
__sub   -- 减法
__div   -- 除法
__unm   -- 相反数
__mod   -- 取模
__pow   -- 乘幂
__concat -- 字段连接
__eq    -- ==
__lt    -- <
__le    -- <=
]]
--

-- -- do test
-- local arr1 = {"a", "b", "c", 1, 2, 3}
-- local arr2 = {"a", 1}
-- local s1 = {}
-- local s2 = {}
-- s1 = Set.new(arr1)
-- s2 = Set.new(arr2)

-- print("Set1 is \t" .. Set.tostring(s1))
-- print("Set2 is \t" .. Set.tostring(s2))
-- print("Set.union is \t" .. Set.tostring(Set.union(s1, s2)))
-- print("Set.intersection is \t" .. Set.tostring(Set.intersection(s1, s2)))
