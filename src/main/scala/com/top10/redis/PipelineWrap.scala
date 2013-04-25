package com.top10.redis

import redis.clients.jedis.SortingParams
import com.top10.redis.{ScalaResponse => Response}
import com.top10.redis.ImplicitJedisMappings._

class PipelineWrap (pipe: Pipeline){

  def del(key: String): Response[Long] = pipe.del(key)
  
  def get(key: String): Response[Option[String]] = pipe.get(key)

  def ttl(key: String): Response[Long] = pipe.ttl(key)
  
  def expire(key: String, ttl: Int): Response[Long] = pipe.expire(key, ttl)
  
  def set(key: String, value: String): Response[String] = pipe.set(key, value)
  
  def setnx(key: String, value: String): Response[Long] = pipe.setnx(key, value)
  
  def exists(key: String): Response[Boolean] = pipe.exists(key)
  
  def getset(key: String, field: String): Response[String] = pipe.getset(key, field)
  
  def hget(key: String, field: String): Response[String] = pipe.hget(key, field)
  
  def hexists(key: String, field: String): Response[Boolean] = pipe.hexists(key, field)

  def hgetAll(key: String): Response[Map[String, String]] = pipe.hgetAll(key)
  
  def hincrBy(key: String, field: String, increment: Long): Response[Long] = pipe.hincrBy(key, field, increment)
  
  def hset(key: String, field: String, value: String): Response[Long] = pipe.hset(key, field, value)
  
  def hdel(key: String, field: String): Response[Long] = pipe.hdel(key, field)
  
  def hmget(key: String, fields: List[String]): Response[Seq[String]] = pipe.hmget(key, fields)
  
  def hmset(key: String, details: Map[String, String]): Response[String] = pipe.hmset(key, details)

  def incrby(key: String, increment: Int): ScalaResponse[Long] = pipe.incrby(key, increment)

  def incr(key: String): Response[Long] = pipe.incr(key)
  
  def put(key: String, values: Map[String, String]): Response[String] = pipe.put(key, values)
  
  def smembers(key: String): Response[Set[String]] = pipe.smembers(key)
  
  def scard(key: String): Response[Long] = pipe.scard(key)
  
  def sadd(key: String, value: String): Response[Long] = pipe.sadd(key, value)
  
  def sismember(key: String, value: String): Response[Boolean] = pipe.sismember(key, value)
  
  def srem(key: String, value: String): Response[Long] = pipe.srem(key, value)
  
  def lpop(key: String): Response[String] = pipe.lpop(key)
  
  def sort(key: String, sp: SortingParams): Response[Seq[String]] = pipe.sort(key, sp)
  
  def llength(key: String): Response[Long] = pipe.llength(key)
  
  def lrange(key: String, start: Long, end: Long): Response[Seq[String]] = pipe.lrange(key, start, end)
  
  def rpush(key: String, value: String): Response[Long] = pipe.rpush(key, value)
  
  def lpush(key: String, value: String): Response[Long] = pipe.lpush(key, value)
  
  def ltrim(key: String, start: Long, end: Long): Response[String] = pipe.ltrim(key, start, end)
  
  def lrem(key: String, value: String, num: Long): Response[Long] = pipe.lrem(key, value, num)
  
  def zadd(key: String, score: Double, value: String): Response[Long] = pipe.zadd(key, score, value)
  
  def zrem(key: String, member: String): Response[Long] = pipe.zrem(key, member)

  def zrevrange(key: String, start: Int, end: Int): ScalaResponse[Set[String]] = pipe.zrevrange(key, start, end)

  def zrevrangeWithScores(key: String, start: Int, end: Int): ScalaResponse[Map[String, Double]]= pipe.zrevrangeWithScores(key, start, end)

  def zrange(key: String, start: Int, end: Int): ScalaResponse[Set[String]] = pipe.zrange(key, start, end)

  def zrangeWithScores(key: String, start: Int, end: Int): ScalaResponse[Map[String, Double]]= pipe.zrevrangeWithScores(key, start, end)
  
  def zrank(key: String, member: String): Response[Long] = pipe.zrank(key, member)
  
  def zrevrank(key: String, member: String): Response[Long] = pipe.zrevrank(key, member)
  
  def zcard(key: String): Response[Long] = pipe.zcard(key)
  
  def zscore(key: String, member: String): Response[Double] = pipe.zscore(key, member)
  
  def zincrBy(key: String, increment: Double, member: String): Response[Double] = pipe.zincrBy(key, increment, member)

}