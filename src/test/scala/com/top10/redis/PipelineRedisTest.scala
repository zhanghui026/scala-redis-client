package com.top10.redis

import org.scalatest.junit.{ShouldMatchersForJUnit, JUnitSuite}
import com.top10.redis.test.RedisTestHelper
import org.junit.{Test, Before}

class PipelineRedisTest extends JUnitSuite with ShouldMatchersForJUnit with RedisTestHelper {

  @Before def setup() {
    ensureRedis
    redis.flushAll
  }

  @Test def testNullZscore() =
    redis.syncAndReturn1(_.zscore("whatevs", "yourmum")) should be (None)

  @Test def testNullGet() =
    redis.syncAndReturn1(_.get("whatevs")) should be (None)

  @Test def testNullZrank() =
    redis.syncAndReturn1(_.zrank("whatevs", "yourmum")) should be (None)

  @Test def testNullRevZrank() =
    redis.syncAndReturn1(_.zrevrank("whatevs", "yourmum")) should be (None)

  @Test def testNullHget() =
    redis.syncAndReturn1(_.hget("whatevs", "yourmum")) should be (None)

  @Test def testNullHmget() =
    redis.syncAndReturn1(_.hmget("whatevs", List("f1", "f2"))) should be (Seq(None, None))

  @Test def testNullLpop() =
    redis.syncAndReturn1(_.lpop("whatevs")) should be (None)

  @Test def testZeroZcard() =
    redis.syncAndReturn1(_.zcard("whatev")) should be (0)

  @Test def testEmptyHGetAll() =
    redis.syncAndReturn1(_.hgetAll("whatevs")) should be (Map())

 

  @Test def testZUnionStore {
    val items = Seq(("a", 1.0), ("c", 3.0))
    val items2 = Seq(("b", 2.0), ("d", 4.0))

    redis.exec(pipeline => items foreach {
      case (key, score) => pipeline.zadd("test_z_1", score, key)
    })

    redis.exec(pipeline => items2 foreach {
      case (key, score) => pipeline.zadd("test_z_2", score, key)
    })

    redis.execSingle(_.zunionStore("test_z_3", Seq("test_z_1", "test_z_2")))

    redis.zrange("test_z_3", 0, -1) should be (Seq("a", "b", "c", "d"))
  }

}