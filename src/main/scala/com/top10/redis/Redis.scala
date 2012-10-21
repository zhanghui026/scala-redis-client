package com.top10.redis

import redis.clients.jedis.JedisCommands

trait Redis {
  
  def ping()
  
  def del(key: String): Long
  
  def get(key: String): Option[String]
  
  def ttl(key: String): Long
  
  def expire(key: String, ttl: Int): Long
  
  def set(key: String, value: String): String
  
  def setnx(key: String, value: String): Long
  
  def exists(key: String): Boolean
  
  def getset(key: String, field: String): Option[String]
  
  def hget(key: String, field: String): Option[String]
  
  def hexists(key: String, field: String): Boolean

  def hgetAll(key: String): Map[String, String]
  
  def hgetMany(keys: Seq[String]): Seq[Map[String, String]]
  
  def hincrBy(key: String, field: String, increment: Long): Long
  
  def hset(key: String, field: String, value: String): Long
  
  def hdel(key: String, field: String): Long
  
  def hmget(key: String, fields: Seq[String]): Seq[Option[String]]
  
  def hmset(key: String, details: Map[String, String]): String
  
  def keys(pattern: String): Set[String]
  
  def put(key: String, values: Map[String, String]): String
  
  def smembers(key: String): Set[String]
  
  def scard(key: String): Long
  
  def sadd(key: String, value: String): Long
  
  def sismember(key: String, value: String): Boolean
  
  def srem(key: String, value: String): Long
  
  def lpop(key: String): Option[String]
  
  def llength(key: String): Long
  
  def lrange(key: String, start: Long, end: Long): List[String]
  
  def ltrim(key: String, start: Long, end: Long): String
  
  def lrem(key: String, value: String, number: Long): Long
  
  def rpush(key: String, value: String): Long
  
  def lpush(key: String, value: String): Long
  
  def zadd(key: String, score: Double, value: String): Long
  
  def zrem(key: String, member: String): Long
  
  def zrevrange(key: String, start: Int, end: Int): List[String]
  
  def zrevrangeWithScores(key: String, start: Int, end: Int): List[(String, Double)]
  
  def zrange(key: String, start: Int, end: Int): List[String]
  
  def zrangeWithScores(key: String, start: Int, end: Int): List[(String, Double)]
  
  def zrank(key: String, member: String): Option[Long]
  
  def zrevrank(key: String, member: String): Option[Long]
  
  def zcard(key: String): Long
  
  def zscore(key: String, member: String): Option[Double]
  
  def zincrBy(key: String, increment: Double, member: String): Double
  
  def flushAll: String
  
  def exec(task: (Pipeline) => Unit)
  
  def syncAndReturnAll(task: (Pipeline) => Unit): List[AnyRef]
  
  def shutdown
}

case class UnspportedShardedOperation(op: String) extends Exception("Sharded implementation doesn't support: "+op)