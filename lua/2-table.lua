
-- stack
local stack 
for i=1,10 do
    stack = {next=stack, value=i}
end 
function iterator(input)
   local l =input 
   while l do 
       print(l.value)
       l = l.next
   end 
end

--
-- 多参数及多返回值
--
function multi_params_return(...)
    
    print({...})
    print(#{...})
    local t
    -- 迭代可变参数,如果参数中存在nil则迭代结束.
    for i,v in ipairs{...} do
        if t then
            t = t .."-"..v
        else
            t = v 
        end
    end
    print(t)

    -- 迭代可变参数且参数中存在nil
    local v
    for i=1,select("#", ...) do
        local arg = select(i, ...)
        if arg then
            if v then
                v = v.."->"..arg
            else
                v = arg
            end
        end
    end
    print(v)
    local v
    -- 同上,直接取得可变参数长度进行迭代
    for i=1,#{...} do
        local arg = select(i,...)
        if arg then
            if v then
                v = v.."=>"..arg
            else
                v = arg
            end
        end
    end
    print(v)
    -- 将可变参数返回
    -- print(type(...))
    -- print(type({...}))
    return ...
end

days = {"Sunday", "Monday", "Tuesday", "Wedensday", "Thursday", "Friday", "Saturday"}
for i,v in ipairs(days) do
    print(i,v)
end
print("\n")
iterator(stack)

-- 带有nil的可变参数
print(multi_params_return(1,2,3,nil,5,6,7))
print(multi_params_return("a","b","c",nil,"d"))
