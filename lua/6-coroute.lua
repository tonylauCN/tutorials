-- ccoroutine
co =
    coroutine.create(
    function()
        print("Hi")
    end
)

print(co)
print(coroutine.status(co))
coroutine.resume(co)
print(coroutine.status(co))

co =
    coroutine.create(
    function(a, b, c)
        for i = 1, 10 do
            print("co", i, a, b, c)
            coroutine.yield(i, a + b + c + i)
        end
    end
)

print(coroutine.status(co))
local status, i, sum = coroutine.resume(co, 1, 2, 3)
print(status, i, sum)
status, i, sum = coroutine.resume(co)
print(status, i, sum)
print(coroutine.status(co))

function producer()
    while true do
        local x = io.read()
        send(x)
    end
end

function consumer()
    while true do
        local x = receive()
        io.write(x, "\n")
    end
end

function receive()
    local status, value = coroutine.resume(producer)
    return value
end

function send(x)
    coroutine.yield(x)
end

producer =
    coroutine.create(
    function()
        while true do
            local x = io.read()
            send(x)
        end
    end
)
