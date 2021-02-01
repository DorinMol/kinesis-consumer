package model

import org.bson.types.ObjectId
import play.api.libs.json.{JsResult, JsValue, Reads}

case class ModelEvent(id: Option[ObjectId], eventType: String, timestamp: Long, value: Double)

object ModelEvent {
  implicit val jsonFormat: Reads[ModelEvent] = new Reads[ModelEvent] {
//    override def writes(o: ModelEvent): JsValue = {
//      Json.toJson(
//        Map(
//          "_id" -> JsString(o.id.toString),
//          "type" -> JsString(o.eventType),
//          "timestamp" -> JsNumber(o.timestamp),
//          "value" -> JsNumber(o.value)
//        )
//      )
//    }

    override def reads(json: JsValue): JsResult[ModelEvent] = {
      val validateEventType = (json \ "type").validate[String]
      val validateTimestamp = (json \ "timestamp").validate[Long]
      val validateValue = (json \ "value").validate[Double]
      for {
        eventType <- validateEventType
        timestamp <- validateTimestamp
        value <- validateValue
      } yield ModelEvent(None, eventType, timestamp, value)
    }
  }
}
