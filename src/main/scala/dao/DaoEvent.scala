package dao

import model.ModelEvent
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.result.InsertManyResult

import scala.concurrent.{ExecutionContext, Future}

class DaoEvent()(implicit mongoDatabase: MongoDatabase, ec: ExecutionContext) {

  def buildDocument(event: ModelEvent): Document = {
    Document("type" -> event.eventType, "timestamp" -> event.timestamp, "value" -> event.value)
  }

  def insert(event: ModelEvent): Future[ModelEvent] = {
    mongoDatabase
      .getCollection("eventlogs")
      .insertOne(buildDocument(event))
      .toFuture()
      .map{ r =>
        val id = r.getInsertedId.asObjectId()
        event.copy(id = Some(id.getValue))
      }
  }

  def insertMany(event: Seq[ModelEvent]): Future[InsertManyResult] = {
    mongoDatabase
      .getCollection("eventlogs")
      .insertMany(event.map(buildDocument))
      .toFuture()
  }
}
