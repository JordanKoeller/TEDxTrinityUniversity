package controllers

import scala.concurrent.{ExecutionContext, Future}
import javax.inject._
import play.api.mvc._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import slick.jdbc.JdbcProfile
import play.twirl.api.Html
import model2.Tables._



@Singleton
class Application @Inject() (
                              protected val dbConfigProvider: DatabaseConfigProvider,
                              cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val formAccepter = new FormAccepter(profile)
//  private val eventDBBuffer = new DBBuffer[EventRow]()
//  private val teamDBBuffer = new DBBuffer[TeamMemberRow]()

  def addEventToDB = Action { request =>
    //    try {
    val event = request.body.asJson.get
    val tableRow = formAccepter.parseEvent((event \ "event").get)
    val speakers = formAccepter.parseSpeakers((event \ "speakers").get,tableRow.id)
    val query = db.run(Event += tableRow)
    println("Sent off table addition")
    val speakerQuery = db.run(Speakers ++= speakers.toIterable)
    println("Sent off Speaker addition")
    query.map{id =>
      println("Query update returned " + id)
    }
    speakerQuery.map{id =>
      println("Finished adding speakers")
    }
    Ok
  }

  def postMember = Action { request =>
    val info = request.body.asJson
    val member = formAccepter.parseMember(info.get)
    println("Parsed the member")
    val query = db.run(TeamMember += member)
    query.map{id =>
      println("Finished adding team member")
    }
    Ok
  }




  def index = Action {
    val title = "This is a test of the view"
    val body = new Html("<p>This is a test area.</p>")
    Ok(views.html.main(title,body))
  }

  def ourTeam = Action.async {
    val members = db.run(TeamMember.filter(_.isActive === 1).result)
    members.map{ret =>
      val page = ret.seq.foldLeft(new Html("")){(old:Html,member:TeamMemberRow) =>
        new Html(old.body + viewstyles.html.namecard(member))
      }
      Ok(views.html.main("Our Team",page))
    }
  }

  def aboutTed = Action {
    val content = views.html.aboutTed()
    Ok(views.html.main("About Ted",content))
  }
//
  def upcomingEvent = Action.async {
    //    val tmp_post = EventRow(12,"This is the title",Some("This is the subtitle"),
    //    "This is the body of the event.","Steiren",null,null,12,10,"This is the link",Some("assets/images/banner.jpg"))
    //    val eventPg = viewstyles.html.event(tmp_post)
    try {
      val id = 0
      val event = db.run(Event.filter(_.id === id).result)
      event.map { e =>
        val item = e.seq.head
        val eventID = item.id
        val speakers = db.run(Speakers.filter(_.eventId === eventID).result)
        speakers.map { speakerSeq =>
          val page = viewstyles.html.event(item, speakerSeq)
          Ok(views.html.main("Upcoming Event", page))
        }
      }.flatten
    }
    catch {
      case e: java.lang.UnsupportedOperationException => Future {
        Ok(views.html.main("No Upcoming Events", new Html("")))
      }
    }
  }
}