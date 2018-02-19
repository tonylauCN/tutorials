

-- print type of type
print(type("Hello World!"))
print(type(1.02e10))
print(type(nil))
print(type(value))
value = 100
print(type(value))
value = "string"
print(type(value))
print(type(true))
print(type(print))
print(type(type(x)))

p = print
p(p)
p(type(p))

-- print type
-- number
print(5e+3)
print(1.5e-3)

print(10000.0+100000.0)

-- string
s = "Hi World!"
s = string.gsub( s,"Hi","Hello")
print(s)
s = string.gsub(s, "Hello", function(c)
    print(c)
    return "Hi"
    end)
print(s)

-- line = io.read(...)
line = 10
n = tonumber(line)
if n==nil then
    error(line.." is not a valid number")
else
    print(n*2)
end

-- table
t = {}
k = "v"
t[k] = 10
t[20] = "great"
print(t["v"])
t[k] = t[k] + 1
print(t[k])
a = t
t = nil
print(a[k])
-- a = nil
-- print(a[k])
for i=1,100 do
    a[i] = i
end
a["x"] = a[10]
a["y"] = nil
print(a.x)
print(a.y)
-- table
a = {x=10,y=10}
a.y = 100
print(a.x..":".. a.y)

local stack = {}
for i=1,10 do
    stack = {next = stack, value = i}
end 

local list = {next=list,value=1}
local l = list
while l do 
    print(l.value)
    l = l.next
end
