package controllers




import scala.concurrent.{ExecutionContext, Future}
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import play.twirl.api.Html
import model2.Tables._
import util._

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
      val speakerAction = (Speakers ++= speakers)
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
    try {
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
    catch {
      case e:Throwable => {
        println("Unable to find a teammember. Submitter does not have permission to post.")
        throw new IllegalArgumentException()
      }
    }
  }




  def index = Action {
    val title = "This is a test of the view"
    val body = new Html("<p>This is a test area.</p>")
    Ok(views.html.main(title,body))
  }

  def ourTeam = Action.async {
    val members = db.run(TeamMember.filter(_.isActive === 1).result)
    members.map{ret =>
      val page = ret.seq.sortBy(_.name.split(" ").last).foldLeft(new Html("")){(old:Html,member:TeamMemberRow) =>
        new Html(old.body + views.html.namecard(member))
      }
      Ok(views.html.main("Our Team",page))
    }
  }



  def newsFeed = Action.async {
    val query = NewsletterPost joinLeft TeamMember on (_.userId === _.id)
    val result = db.run(query.result)
    result.map{q =>
      val page = q.foldLeft(new Html("")){(old,res) =>
        val post = res._1
        //We assume it is a success
        val poster = res._2.get
        val view = views.html.newsPost(post,poster)
        new Html(old.body + view)
      }
      Ok(views.html.main("Newsletter",page))
    }
  }

  def aboutTed = Action {
    val content = views.html.aboutTed()
    Ok(views.html.main("About Ted",content))
  }
  //
  def upcomingEvent = Action.async {
    try {
      val joined = Event joinLeft Speakers on (_.id === _.eventId)
      val query = db.run(joined.result)
      query.map{q =>
        val events = q.groupBy(_._1.id)
        val pages = events.foldLeft(new Html("")){(html,kv) =>
          //          val speakers = kv._2.flatMap{e => Seq(e._2.getOrElse(null))}.filter(_ != null).sortBy(_.name.split(" ").last)
          val speaks = kv._2.flatMap{e => Seq(e._2.getOrElse(null))}.filter(_ != null).sortBy(_.name.split(" ").last)
          val banned = Array(16,20,22,24)
          val speakers = speaks.filterNot(elem => banned.contains(elem.speakerId))
          val speakerCards = speakers.map(e => views.html.speakernamecard(e))
          val pg = views.html.event(kv._2.head._1,speakers)
          val speakerCardsHtml = speakerCards.foldLeft(new Html("")){(h1,k1) => new Html(h1.body + k1.body)}
          new Html(html.body + pg.body + speakerCardsHtml)
        }
        Ok(views.html.main("Upcoming Events", pages))
      }
    }
    catch {
      case e: java.lang.UnsupportedOperationException => Future {
        Ok(views.html.main("No Upcoming Events", new Html("")))
      }
    }
  }


  def sponsors = Action {
    val trinity = views.html.CaptionedImage("https://new.trinity.edu/","assets/images/TULogo.png")
    val bertsch = views.html.CaptionedImage("https://www.assistingseniors.com/","assets/images/Bertsch.png")
    Ok(views.html.main("Sponsors",Seq(trinity,bertsch)))
  }


}