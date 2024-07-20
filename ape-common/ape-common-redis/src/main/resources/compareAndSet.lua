local key = KEYS[1]
local oldValue = ARGV[1]
local newValue = ARGV[2]

local redisValue = redis.call('get',key)
if ( redisValue == false or redisValue == oldValue)
then
    redis.call('set',key,newValue)
    return true
else
    return false
end