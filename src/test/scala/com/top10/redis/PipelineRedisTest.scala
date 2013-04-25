package com.top10.redis

import org.scalatest.junit.{ShouldMatchersForJUnit, JUnitSuite}
import com.top10.redis.test.RedisTestHelper
import org.junit.{Test, Before}

class PipelineRedisTest extends JUnitSuite with ShouldMatchersForJUnit with RedisTestHelper {

  @Before def setup() {
    ensureRedis
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

}