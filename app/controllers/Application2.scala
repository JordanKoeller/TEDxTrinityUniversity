package controllers

import scala.concurrent.ExecutionContext

import javax.inject._
import play.api.mvc._

import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import slick.jdbc.JdbcProfile
import play.twirl.api.Html

import model2.Tables._
import model2.DBBuffer



@Singleton
class Application @Inject() (
                              protected val dbConfigProvider: DatabaseConfigProvider,
                              cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val formAccepter = new FormAccepter(profile)
  private val eventDBBuffer = new DBBuffer[EventRow]()
  private val teamDBBuffer = new DBBuffer[TeamMemberRow]()

  def addEventToDB = Action { request =>
    //    try {
    println("Received ping to add an event to db")
    val event = request.body.asJson
    val tableRow = formAccepter.parseEvent(event.get)
    val speakers = formAccepter.parseSpeakers(event.get,tableRow.id)
    println("Constructed Row")
    val query = db.run(Event += tableRow)
    val speakerQuery = db.run(Speakers ++= speakers.toIterable)
    println("Passed ddb.run")
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
      println("Finisheed adding team member")
    }
    Ok
  }




  def index = Action {
    val title = "This is a test of the view"
    val body = new Html("<p>This is a test area.</p>")
    Ok(views.html.main(title,body))
  }

  def ourTeam = Action {
    val teamMembers = teamDBBuffer.items
    val tiles = teamMembers.foldLeft(new Html("")){(old:Html,member:TeamMemberRow) =>
      new Html(old.body + viewstyles.html.namecard(member).body)
    }
    Ok(views.html.main("Our Team",tiles))
  }

  def aboutTed = Action {
    val content = views.html.aboutTed()
    Ok(views.html.main("About Ted",content))
  }
//
//  def upcomingEvents = Action {
//    val events = eventDBBuffer.items
//    val posts = events.foldLeft(new Html("")){ (old,dbEntry) =>
//      new Html(old.body + views.html.postCard(dbEntry))
//    }
//      Ok(views.html.main("Upcoming Events",posts))
//  }
}