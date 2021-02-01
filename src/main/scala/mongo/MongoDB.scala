package mongo

import config.ConfigConsumer
import org.mongodb.scala.{MongoClient, MongoDatabase}

class MongoDB()(implicit config: ConfigConsumer) {
  private val mongoClient: MongoClient = {
    System.setProperty("org.mongodb.async.type", "netty")
    MongoClient(config.mongoUri)
  }
  val database: MongoDatabase = mongoClient.getDatabase(config.mongoDbName)
}
