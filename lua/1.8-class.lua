
-- self
Account = {balance = 100}
local mt = {__index = Account}
function Account:draw(v)
    self.balance = self.balance - v
end
function Account:new(self)
    return setmetatable({}, mt)
end

local a = Account:new()
local b = Account:new()
Account = nil
a:draw(10)
b:draw(40)
print(a.balance)
print(b.balance)

-- private field
function newAccound(initbalance)
    local self = {balance = initbalance}

    local withdraw = function(v)
        self.balance = self.balance - v
    end
    local deposit = function(v)
        self.balance = self.balance + v
    end
    local getBalance = function()
        return self.balance
    end
    return {
        withdraw = withdraw,
        deposit = deposit,
        getBalance = getBalance}
end


a = newAccound(100)
a.withdraw(10)
a.deposit(5)
print(a:getBalance())