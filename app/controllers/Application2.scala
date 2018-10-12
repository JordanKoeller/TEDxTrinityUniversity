package controllers




import scala.concurrent.{ExecutionContext, Future}
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import play.twirl.api.Html
import model2.Tables._


@Singleton
class Application @Inject() (
                              protected val dbConfigProvider: DatabaseConfigProvider,
                              cc: ControllerComponents)(implicit ec:ExecutionContext)
  extends AbstractController(cc)  {
  import profile.api._
  val db = Database.forConfig("mydb")

  private val formAccepter = new FormAccepter(profile)

  def addEventToDB = Action { request =>
    //    try {
    val event = request.body.asJson.get
    val tableRow = formAccepter.parseEvent((event \ "event").get)
    val eventAction = Event returning Event.map(_.id) += tableRow
    val query = db.run(eventAction)
    query.map{id =>
      val speakers = formAccepter.parseSpeakers((event \ "speakers").get,id)
      val speakerAction = Speakers ++= speakers
      db.run(speakerAction)
    }
    Ok
  }
  def postMember = Action { request =>
    val info = request.body.asJson
    val member = formAccepter.parseMember(info.get)
    db.run(TeamMember += member)
    Ok
  }

  def postArticle = Action { request =>
    val info = request.body.asJson
    val (post,email) = formAccepter.parseNewsArticle(info.get,db)
    val memberQuery = db.run(TeamMember.filter(_.email === email).result)
    memberQuery.map{ret =>
      val id = ret.head.id
      val memberInsert = NewsletterPostRow(post.id,
        post.postDate,
        post.description,
        id,
        post.title,
        post.subtitle,
        post.`abstract`,
        post.mediaLink)
      db.run(NewsletterPost += memberInsert)
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
    try {
      val id = 1
      val event = db.run(Event.filter(_.id === id).result)
      event.map { e =>
        val item = e.seq.head
        val eventID = item.id
        val speakers = db.run(Speakers.filter(_.eventId === eventID).result)
        speakers.map { speakerSeq =>
          val page = viewstyles.html.event(item, speakerSeq)
          Ok(views.html.main("Upcoming Events", page))
        }
      }.flatten
    }
    catch {
      case e: java.lang.UnsupportedOperationException => Future {
        Ok(views.html.main("No Upcoming Events", new Html("")))
      }
    }
  }

  def sponsors = Action {
    val window = viewstyles.html.CaptionedImage()
    Ok(views.html.main("Sponsors",window))
  }

  def speaker(id:Int) = Action.async {
    val speakers = db.run(Speakers.filter(_.eventId === id).result)
    speakers.map { ret =>
      val namecards = ret.seq.foldLeft(new Html("")) { (old: Html, speaker: SpeakersRow) =>
        new Html(old.body + viewstyles.html.speakernamecard(speaker))
      }
      Ok(views.html.main("Event Speakers", namecards))
    }
  }
}