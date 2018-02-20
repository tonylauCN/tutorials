
-- error
local status, err = pcall(function() error({code=200, msg="not found"}) end)
print(status)
if not status then
    print(err.msg)
end
if err then
    print(err.code)
end

-- trace
--print(debug.debug())
print(debug.traceback())