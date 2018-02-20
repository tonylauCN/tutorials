-- buffer
local t = {}

-- io.open("/Users/ryuu/git/tutorials/lua/hello.sh")
os.execute("pwd")
-- print(os.date({format="yyyy-MM-dd"}))
for line in io.lines("/Users/ryuu/git/tutorials/lua/hello.sh") do
    t[#t + 1] = line
end
-- 防止移动
t[#t + 1] = ""
--local content = table.concat(t, "\n") .. "\n"
local content = table.concat(t, "\n")

print(content)
