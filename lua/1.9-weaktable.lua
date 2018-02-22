
--
-- 弱引用sample
--
a = {}
-- "k"=所有key, "v"=所有value, "kv"=table完全为弱引用
b = {__mode = "k"}
-- a中所有key为弱引用
setmetatable(a, b)
key = {}
a[key] = 1
key = {}
a[key] = 2
-- 强制垃圾收集
collectgarbage()
-- 第一个key被清除

