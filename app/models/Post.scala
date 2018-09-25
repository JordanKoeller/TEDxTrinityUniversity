package models

import java.time.LocalDateTime
import model2.TeamMember

trait Post {

  val title: String
  val subtitle: Option[String]
  val author: TeamMember
  val description: String
  val date: LocalDateTime

  def dayOfWeek: String = {



    val dayNum = date.getDayOfWeek.getValue
    dayNum match {
      case 1 => "Monday"
      case 2 => "Tuesday"
      case 3 => "Wednesday"
      case 4 => "Thursday"
      case 5 => "Friday"
      case 6 => "Saturday"
      case 7 => "Sunday"
    }
  }

  def dateNumberString(delimiter: String = "/"): String = {
    val month = date.getMonth.getValue
    val day = date.getDayOfMonth
    val year = date.getYear
    month + delimiter + day + delimiter + year
  }

  def monthSuffix: String = {
    val month = date.getMonth.getValue
    month match {
      case 1 => "uary"
      case 2 => "ruary"
      case 3 => ""
      case 4 => ""
      case 5 => ""
      case 6 => ""
      case 7 => "y"
      case 8 => "ust"
      case 9 => "ember"
      case 10 => "ober"
      case 11 => "ember"
      case 12 => "ember"
    }

  }

  def monthPrefix: String = {
    val month = date.getMonth.getValue
    month match {
      case 1 => "Jan"
      case 2 => "Feb"
      case 3 => "March"
      case 4 => "April"
      case 5 => "May"
      case 6 => "June"
      case 7 => "Jul"
      case 8 => "Aug"
      case 9 => "Sept"
      case 10 => "Oct"
      case 11 => "Nov"
      case 12 => "Dec"
    }
  }
}