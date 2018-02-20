-- serialize

function basicSerialize(o)
    if type(o) == "number" then
        return tostring(o)
    else
        return string.format("%q", o)
    end
end

function save(name, value, saved)
    saved = saved or {}
    io.write(name, " = ")

    if type(value) == "number" or type(value) == "string" then
        io.write(basicSerialize(value), "\n")
    elseif type(value) == "table" then
        if saved[value] then
            io.write(saved[value], "\n")
        else
            saved[value] = name
            io.write("{}", "\n")
            for k, v in pairs(value) do
                k = basicSerialize(k)
                local fname = string.format("%s[%s]", name, k)
                save(fname, v, saved)
            end
        end
    else
        error("can not save a " .. type(value))
    end
end

-- serialize test
a = {x = 1, y = 2; {3, 4, 5}}
a[2] = a
a.z = a[1]

save("a", a)
