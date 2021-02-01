package service

import akka.{Done, NotUsed}
import akka.stream.Materializer
import akka.stream.alpakka.kinesis.scaladsl.KinesisSource
import akka.stream.alpakka.kinesis.{ShardIterator, ShardSettings}
import akka.stream.scaladsl.Source
import config.ConfigConsumer
import dao.DaoEvent
import model.ModelEvent
import play.api.libs.json.Json
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.Record

import scala.concurrent.Future
import scala.concurrent.duration._

class ServiceConsumer()(implicit
                        config: ConfigConsumer,
                        daoEvent: DaoEvent,
                        amazonKinesisAsync: KinesisAsyncClient,
                        materializer: Materializer) {
  private val settings =
    ShardSettings(streamName = config.streamName, "shardId-000000000000")
      .withRefreshInterval(1.second)
      .withLimit(500)
      .withShardIterator(ShardIterator.TrimHorizon)

  def consume: Future[Done] = {
    val source: Source[software.amazon.awssdk.services.kinesis.model.Record, NotUsed] =
      KinesisSource.basic(settings, amazonKinesisAsync)

    source
      .grouped(config.insertBatchNumber)
      .mapAsync(config.parallelism) {records =>
        val events = records.flatMap(recordToEvent)
        daoEvent.insertMany(events)
      }
      .runForeach { inserted =>
        println(s"Inserted ${inserted.getInsertedIds.size}")
      }
  }

  private def recordToEvent(record: Record): Option[ModelEvent] = {
    val bytes = record.data().asByteArray()
    val str = new String(bytes)
    Json.parse(str).validate[ModelEvent].asOpt
  }
}
