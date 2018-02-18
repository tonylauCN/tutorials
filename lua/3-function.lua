--
-- function 函数
--
fun={p=print}
fun.p("function")
-- 相当于方法重载
print=math.sin
fun.p(print(1.5))
sin = fun.p
sin(10,20)

local x = {
    {name="A", ip="10.10.0.1"},
    {name="B", ip="127.0.0.1"},
    {name="C", ip="192.168.0.1"},
    {name="D", ip="203.16.205.19"}}
-- 倒序，修改输入对象结构
table.sort(x, function(a,b) return a.name>b.name end)

local function iterator(arr,  fun)
    local name = "name"
    for k,v in ipairs(arr) do
       fun(k,v[name], v.ip) 
    end
end
-- 类似充血模式
x.p = iterator
x.p(x, fun.p)

-- 匿名函数
local function counter()
    local i = 0
    return function ()
        i = i+1
        return i
    end
end

local c = counter()
fun.p(c())
fun.p(c())
fun.p(c())


print = fun.p

print(_VERSION)


