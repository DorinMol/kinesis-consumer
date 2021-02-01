package config

import com.typesafe.config.ConfigFactory

class ConfigConsumer {
  private val config = ConfigFactory.load
  val streamName: String = config.getString("kinesis-consumer.stream-name")
  val insertBatchNumber: Int = config.getInt("kinesis-consumer.insert-batch-number")
  val parallelism: Int = config.getInt("kinesis-consumer.parallelism")
  val mongoUri: String = config.getString("mongo.uri")
  val mongoDbName: String = config.getString("mongo.db-name")
}
