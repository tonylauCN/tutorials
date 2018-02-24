
require('mobdebug').start('localhost')
local redis = require "resty.redis"
local red = redis:new()

red:set_time(1000)

local ok, err = red:connect("127.0.0.1", 6179)
if not ok then
    ngx.say("failed to connect: ", err)
    return
end
ngx.say("successed to connect: ", ok)
require('mobdebug').done()