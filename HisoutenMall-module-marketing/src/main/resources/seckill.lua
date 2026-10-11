-- KEYS[1] = seckill:stock:{skuId}
-- KEYS[2] = seckill:users:{skuId}
-- ARGV[1] = userId
-- ARGV[2] = 本次购买数量（通常是 1）
-- ARGV[3] = 每人限购数量

local stock = tonumber(redis.call('GET', KEYS[1]))
if stock == nil or stock < tonumber(ARGV[2]) then
    return 1   -- 库存不足
end

local bought = tonumber(redis.call('HGET', KEYS[2], ARGV[1]) or 0)
if bought + tonumber(ARGV[2]) > tonumber(ARGV[3]) then
    return 2   -- 超过限购数
end

redis.call('DECRBY', KEYS[1], ARGV[2])
redis.call('HINCRBY', KEYS[2], ARGV[1], ARGV[2])

return 0