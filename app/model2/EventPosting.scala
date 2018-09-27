package model2

import java.time.LocalDateTime

case class Speaker(name:String,desc:String)


case class EventPosting(val title:String,
    val subtitle:Option[String],
    val desc:String,
    val date:LocalDateTime,
    val venue:String,
    val maxSeats:Int,
    val mediaURL:Option[String],
    val speakers:Array[Speaker])