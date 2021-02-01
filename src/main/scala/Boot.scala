import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.github.matsluni.akkahttpspi.AkkaHttpClient
import config.ConfigConsumer
import dao.DaoEvent
import mongo.MongoDB
import org.mongodb.scala.MongoDatabase
import service.ServiceConsumer
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient

import scala.concurrent.ExecutionContext

object Boot extends App {
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: Materializer = ActorMaterializer()
  implicit val amazonKinesisAsync: KinesisAsyncClient =
    KinesisAsyncClient
      .builder()
      .region(Region.US_EAST_1)
      .httpClient(AkkaHttpClient.builder().withActorSystem(system).build())
      .build()
  system.registerOnTermination(amazonKinesisAsync.close())
  implicit val config: ConfigConsumer = new ConfigConsumer()
  implicit val mongoDB: MongoDatabase = new MongoDB().database
  implicit val daoEvent: DaoEvent = new DaoEvent()

  val serviceConsumer = new ServiceConsumer()
  serviceConsumer.consume.onComplete(_ => println("done"))



}
