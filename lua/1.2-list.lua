
-- stack
list = nil
for i = 1, 10 do
    list = {next = list, value = i}
end
print(list)

-- queue


-- list
List = {}
function List.new()
    return {firest = 0, last = -1}
end
function List.pushfirst(list, value)
    local first = list.first - 1
    
end