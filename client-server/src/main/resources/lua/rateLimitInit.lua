-------------初始化lua-----------------
local result = 1;
redis.pcall("HMSET", KEYS[1],
        "last_refreshed", ARGV[1],
        "current_permits", ARGV[2],
        "capacity", ARGV[3],
        "rate", ARGV[4]
)
return result