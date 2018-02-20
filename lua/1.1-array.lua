arrays = {}
for i = 1, 100 do
    arrays[i] = i
end
print(#arrays)
print(arrays)

local m, n = 10, 10
mt = {}
for i = 1, m do
    mt[i] = {}
    for j = 1, n do
        mt[i][j] = 0
    end
end
print(#mt)
print(#mt[1])