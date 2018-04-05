package models

import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

class TEDEvent(
  val title: String,
  val subtitle: Option[String],
  val date: LocalDateTime,
  val speaker: String,
  val venue: String,
  val maxSeats: Int,
  val description: String,
  val imgURL: Option[String],
  val author:TeamMember = TeamMembers.Anonymous) extends Post {

  private var registeredGuests = 0

  def time: String = {
    val hr = date.getHour
    val min = date.getMinute
    val hrFormatted = hr % 12
    val amPm = if (hr >= 12) "PM" else "AM"
    val minString = if (min < 10) "0" + min else min
    val hrString = if (hrFormatted == 0) "12" else hrFormatted
    s"$hrString:$minString $amPm"
  }


  def addGuest() = {
    if (registeredGuests + 1 > maxSeats) println("EVENT FULL") //hrow EventFullException("")
    else registeredGuests += 1
  }

  def rmGuest() = {
    if (registeredGuests > 0) registeredGuests -= 1
  }

  def numAttendants: Int = {
    registeredGuests
  }

}

object TEDEvent {

  def apply(
    title: String,
    subtitle: String,
    day: Int,
    month: Int,
    year: Int,
    hr: Int,
    min: Int,
    speaker: String,
    venue: String,
    maxSeats: Int,
    description: String,
    imgURL: String = ""): TEDEvent = {
    val date = LocalDateTime.of(year, month, day, hr, min)
    val st = if (subtitle != "") Some(subtitle) else None
    val im = if (imgURL != "") Some(imgURL) else None
    new TEDEvent(title, st, date, speaker, venue, maxSeats, description, im)
  }

  def apply(
    title: String,
    subtitle: String,
    speaker: String,
    desc: String,
    venue: String,
    date: String,
    time: String,
    seats: String,
    link: String): TEDEvent = {
    val split = date.split("-")
    val day = split(2).toInt
    val month = split(1).toInt
    val yr = split(0).toInt
    val timeSplit = time.split(":")
    val hr = timeSplit(0).toInt
    val min = timeSplit(1).toInt
    TEDEvent(title, subtitle, day, month, yr, hr, min, speaker, venue, seats.toInt, desc, link)
  }
  
  def unapply(tev:TEDEvent) = {
    Some(tev.title,
        tev.subtitle.getOrElse(""),
        tev.speaker,
        tev.description,
        tev.venue,
        tev.dateNumberString("/"),
        tev.time,
        tev.maxSeats.toString,
        tev.imgURL.getOrElse(""))
  }

}