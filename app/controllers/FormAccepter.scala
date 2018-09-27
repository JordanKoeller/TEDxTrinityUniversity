package controllers

import java.time.LocalDateTime
import java.sql.Date
import java.sql.Time
import java.time.ZoneId
import java.time.temporal.ChronoField
import play.api.libs.json._
import slick.jdbc.JdbcProfile

import model2.Tables._

class FormAccepter(profile: JdbcProfile) {

  sealed case class EventJSMapper(title:String, subtitle:String, description:String, venue:String,date:String,time:String,numSeats:Int,mediaLink:Option[String])
  sealed case class SpeakerJSMapper(name:String, bio:String)
  sealed case class SpeakerSeqMapper(speakers:Seq[SpeakerJSMapper])
  sealed case class TeamMemberConverter(id:Int,isActive:Int,name:String,position:String,major:String,gradClass:Int,biography:String,email:String,mediaUrl:String)
  implicit val eventConverter = Json.reads[EventJSMapper]
  implicit val speakerConverter = Json.reads[SpeakerJSMapper]
  implicit val speakerCol = Json.reads[SpeakerSeqMapper]
  implicit val teamMemberConverter = Json.reads[TeamMemberConverter]


  def parseEvent(js:JsValue):EventRow = {
    val jsMapper:JsResult[EventJSMapper] = Json.fromJson[EventJSMapper](js)
    jsMapper match {
      case JsSuccess(item:EventJSMapper,path:JsPath) =>
        val date = item.date
        val time = item.time
        val split = date.split("-")
        val day = split(2).toInt
        val month = split(1).toInt
        val year = split(0).toInt
        val timeSplit = time.split(":")
        val hr = timeSplit(0).toInt
        val min = timeSplit(1).toInt
        val dateTime = LocalDateTime.of(year,month,day,hr,min)
        //        val date = event.date.atZone(ZoneId.of("America/Chicago"))
        val epochsec = dateTime.getLong(ChronoField.INSTANT_SECONDS) * 1000
        val sqldate = new Date(epochsec)
        val sqltime = new Time(epochsec)
        val regLink = "#"
        val takenSeats = 0
        val id = 0
        val subOpt = if (item.subtitle != "") Some(item.subtitle) else None
        EventRow(id,item.title,subOpt,item.description,item.venue,sqldate,sqltime,item.numSeats,takenSeats,regLink,item.mediaLink)
      case e: JsError =>
        println("Failed to construct JSValue currectly.")
        null
    }
  }

  def parseSpeakers(js:JsValue,eventID:Int):Array[SpeakersRow] = {
    val jsMapper:JsResult[Seq[SpeakerJSMapper]] = Json.fromJson[Seq[SpeakerJSMapper]](js)
    jsMapper match {
      case JsSuccess(item:Seq[SpeakerJSMapper],path:JsPath) =>
        val tmpId = 0
        val media = ""
        item.map{ elem =>
          SpeakersRow(tmpId,eventID,elem.name,elem.bio,media)
        }.toArray
      case e: JsError =>
        println("Failed to parse the speaker correctly.")
        null
    }

  }

  def parseMember(js:JsValue):TeamMemberRow = {
    val jsMapper:JsResult[TeamMemberConverter] = Json.fromJson[TeamMemberConverter](js)
    jsMapper match {
      case JsSuccess(item:TeamMemberConverter,path:JsPath) =>
        TeamMemberRow(item.id,item.isActive,item.name,item.position,item.major,item.gradClass,item.biography,item.email,item.mediaUrl)
      case e: JsError =>
        println("Couldn't construct team member")
        null
    }
  }
}