package com.top10.redis

import redis.clients.jedis.JedisCommands
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import org.apache.commons.pool.impl.GenericObjectPool

trait Redis {

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

  def incrby(key: String, increment: Int): Long

  def incr(key: String): Long

  def put(key: String, values: Map[String, String]): String

  def smembers(key: String): Set[String]

  def scard(key: String): Long

  def sadd(key: String, value: String): Long

  def sismember(key: String, value: String): Boolean

  def srem(key: String, value: String): Long

  def lpop(key: String): Option[String]

  def llength(key: String): Long

  def lrange(key: String, start: Long, end: Long): Seq[String]

  def ltrim(key: String, start: Long, end: Long): String

  def lrem(key: String, value: String, number: Long): Long

  def lset(key: String, index: Long, value: String): String

  def rpush(key: String, value: String): Long

  def lpush(key: String, value: String): Long

  def zadd(key: String, score: Double, value: String): Long

  def zrem(key: String, member: String): Long

  def zrevrange(key: String, start: Int, end: Int): Seq[String]

  def zrevrangeWithScores(key: String, start: Int, end: Int): Map[String, Double]

  def zrange(key: String, start: Int, end: Int): Seq[String]

  def zrangeWithScores(key: String, start: Int, end: Int): Map[String, Double]

  def zrank(key: String, member: String): Option[Long]

  def zrevrank(key: String, member: String): Option[Long]

  def zcard(key: String): Long

  def zscore(key: String, member: String): Option[Double]

  def zincrBy(key: String, increment: Double, member: String): Double

  def exec(task: (Pipeline) => Unit)

  def syncAndReturnAll(task: (Pipeline) => Unit): Seq[AnyRef]

  def syncAndReturnAllAs[A : Manifest](task: (Pipeline) => Unit): Seq[A] = syncAndReturnAll(task).map(value => RedisJavaMapping.as[A](value))

  def syncAndReturn[A : Manifest, B : Manifest](task: (Pipeline) => Unit): Tuple2[A, B] = {
    val results = syncAndExpect(task, 2)
    (RedisJavaMapping.as[A](results(0)), RedisJavaMapping.as[B](results(1)))
  }
  def syncAndReturn[A : Manifest, B : Manifest, C : Manifest](task: (Pipeline) => Unit): Tuple3[A, B, C] = {
    val results = syncAndExpect(task, 3)
    (RedisJavaMapping.as[A](results(0)), RedisJavaMapping.as[B](results(1)), RedisJavaMapping.as[C](results(2)))
  }
  def syncAndReturn[A : Manifest, B : Manifest, C : Manifest, D : Manifest](task: (Pipeline) => Unit): Tuple4[A, B, C, D] = {
    val results = syncAndExpect(task, 4)
    (RedisJavaMapping.as[A](results(0)), RedisJavaMapping.as[B](results(1)), RedisJavaMapping.as[C](results(2)), RedisJavaMapping.as[D](results(3)))
  }
  def syncAndReturn[A : Manifest, B : Manifest, C : Manifest, D : Manifest, E : Manifest](task: (Pipeline) => Unit): Tuple5[A, B, C, D, E] = {
    val results = syncAndExpect(task, 5)
    (RedisJavaMapping.as[A](results(0)), RedisJavaMapping.as[B](results(1)), RedisJavaMapping.as[C](results(2)), RedisJavaMapping.as[D](results(3)), RedisJavaMapping.as[E](results(4)))
  }
  def syncAndReturn[A : Manifest, B : Manifest, C : Manifest, D : Manifest, E : Manifest, F : Manifest](task: (Pipeline) => Unit): Tuple6[A, B, C, D, E, F] = {
    val results = syncAndExpect(task, 6)
    (RedisJavaMapping.as[A](results(0)), RedisJavaMapping.as[B](results(1)), RedisJavaMapping.as[C](results(2)), RedisJavaMapping.as[D](results(3)), RedisJavaMapping.as[E](results(4)), RedisJavaMapping.as[F](results(5)))
  }
  def syncAndReturn[A : Manifest, B : Manifest, C : Manifest, D : Manifest, E : Manifest, F : Manifest, G : Manifest](task: (Pipeline) => Unit): Tuple7[A, B, C, D, E, F, G] = {
    val results = syncAndExpect(task, 7)
    (RedisJavaMapping.as[A](results(0)), RedisJavaMapping.as[B](results(1)), RedisJavaMapping.as[C](results(2)), RedisJavaMapping.as[D](results(3)), RedisJavaMapping.as[E](results(4)), RedisJavaMapping.as[F](results(5)), RedisJavaMapping.as[G](results(6)))
  }
  def syncAndReturn[A : Manifest, B : Manifest, C : Manifest, D : Manifest, E : Manifest, F : Manifest, G : Manifest, H : Manifest](task: (Pipeline) => Unit): Tuple8[A, B, C, D, E, F, G, H] = {
    val results = syncAndExpect(task, 8)
    (RedisJavaMapping.as[A](results(0)), RedisJavaMapping.as[B](results(1)), RedisJavaMapping.as[C](results(2)), RedisJavaMapping.as[D](results(3)), RedisJavaMapping.as[E](results(4)), RedisJavaMapping.as[F](results(5)), RedisJavaMapping.as[G](results(6)), RedisJavaMapping.as[H](results(7)))
  }
  def syncAndReturn[A : Manifest, B : Manifest, C : Manifest, D : Manifest, E : Manifest, F : Manifest, G : Manifest, H : Manifest, I : Manifest](task: (Pipeline) => Unit): Tuple9[A, B, C, D, E, F, G, H, I] = {
    val results = syncAndExpect(task, 9)
    (RedisJavaMapping.as[A](results(0)), RedisJavaMapping.as[B](results(1)), RedisJavaMapping.as[C](results(2)), RedisJavaMapping.as[D](results(3)), RedisJavaMapping.as[E](results(4)), RedisJavaMapping.as[F](results(5)), RedisJavaMapping.as[G](results(6)), RedisJavaMapping.as[H](results(7)), RedisJavaMapping.as[I](results(8)))
  }

  protected def syncAndExpect(task: (Pipeline) => Unit, count: Int): Seq[AnyRef] = {
    val results: Seq[AnyRef] = syncAndReturnAll(task)
    assert(results.length == count, "syncAndReturnAll returned "+results.length+" results but you were expecting "+count)
    results
  }

  def shutdown
}

object Redis {
  def config(poolSize: Int): JedisPoolConfig = {
    val config = new JedisPoolConfig()
    config.setTestOnBorrow(true)
    config.setTestWhileIdle(true)
    config.setMaxActive(poolSize)
    config.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK)
    config
  }

  def pool(config: JedisPoolConfig, host: String, port: Int, pwd: Option[String], timeout: Int): JedisPool = {
    pwd.map(password => new JedisPool(config, host, port, timeout, password))
       .getOrElse(new JedisPool(config, host, port, timeout))
  }
}

