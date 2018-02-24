-- require("mobdebug").start("localhost")
-- local redis = require "resty.redis"
local redis = require("redis-extra")

local red = redis:new()
red:set_time(1000)

local ok, err = red:connect("127.0.0.1", 6179)
if not ok then
    ngx.say("failed to connect: ", err)
    return
end
local exp = "bitmap.1 | (bitmap.2 & bitmap.3)"
ngx.say("bitmap exp:\t", exp)

local v = red.R.bitcountexp(red, exp)
--v = v(red, "bitmap.1 | bitmap.2 | bitmap.3 | bitmap.4")
-- m.v = _do_cmd(red, "R.BITCOUNTEXP", "bitmap.1 | bitmap.2 | bitmap.3")
ngx.say("bitmap count:\t", v)
v, err = red.R.bitarrayexp(red, exp)
if not v then
    ngx.say(err)
end
ngx.say("bitmap array:\t", v)
-- require("mobdebug").done()
