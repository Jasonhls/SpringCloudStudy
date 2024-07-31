-- KEYS[1]: 令牌桶的key，格式为 "rate_limiter:{bucket_key}"
-- ARGV[1]: 请求的令牌数量
-- ARGV[2]: 当前时间戳（可选，用于避免Redis服务器与客户端时间不同步的问题）

local key = KEYS[1]  --令牌桶的key
local tokens_requested = tonumber(ARGV[1]) --请求的令牌数量
local now = tonumber(ARGV[2]) or redis.call('TIME')[1] --当前时间戳（可选，用于避免Redis服务器与客户端时间不同步的问题）

-- 获取当前桶中的令牌数和上次刷新时间
local ratelimit_info = redis.call("HMGET", key, "last_refreshed", "current_permits", "capacity", "rate")
local last_refreshed = ratelimit_info[1]
local current_permits = tonumber(ratelimit_info[2])
local capacity = tonumber(ratelimit_info[3])  --桶的容量
local fill_rate = tonumber(ratelimit_info[4]) --每秒添加的令牌数量（即速率）

-- 初始化当前桶容量
local local_curr_permits = capacity;

-- 如果上次刷新时间不存在，则初始化为当前时间
if (last_refreshed == nil or type(last_refreshed) =='boolean') then
    last_refreshed = now
    redis.call("HMSET", key, "last_refreshed", now);
else
    --计算该时间间隔内加入了多少令牌
    local reverse_permits = (now - last_refreshed) * fill_rate
    if (reverse_permits > 0) then
        redis.call("HMSET", key, "last_refreshed", now);
    end

    --计算当前总共有多少令牌，期望值
    local expect_curr_permits = reverse_permits + current_permits
    -- 与桶最大容量做对比
    local_curr_permits = math.min(expect_curr_permits, capacity);
end

if (local_curr_permits >= tokens_requested) then
    local_curr_permits = local_curr_permits - tokens_requested
    redis.call("HMSET", key, "current_permits", local_curr_permits);
    return 1 -- 表示成功获取令牌
else
    redis.call("HMSET", key, "current_permits", local_curr_permits);
    return 0 -- 表示获取令牌失败
end