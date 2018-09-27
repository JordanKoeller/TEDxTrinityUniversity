//package controllers
//
//import java.sql.Date
//import java.sql.Time
//import java.time.ZoneId
//import java.time.temporal.ChronoField
//
//import scala.concurrent.ExecutionContext
//
//import javax.inject._
//import models.TEDEvent
//import models.TEDEventList
//import models.Tables.EventDescriptions
//import models.Tables.EventDescriptionsRow
//import model2.TeamMembers
//import play.api.db.slick.DatabaseConfigProvider
//import play.api.db.slick.HasDatabaseConfigProvider
//import play.api.mvc._
//import play.twirl.api.Html
//import slick.jdbc.JdbcProfile
//import scala.concurrent.Await
//import scala.concurrent.duration.Duration
//
////import play.twirl.api.Html
//import util._
//import play.api.data.Forms._
//import play.api.data._
//import models.Newsletter
//
//@Singleton
//class Application @Inject() (
//  protected val dbConfigProvider: DatabaseConfigProvider,
//  cc: ControllerComponents)(implicit ec: ExecutionContext)
//  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {
//  import profile.api._
//
//  profile
//
//  updateModel()
//
//  def index = Action {
//    val events = TEDEventList.list
//    val content = formatEvents(events)
//    Ok(views.html.main("Upcoming Events", content))
//  }
//
//
//
//
//  def formatEvents(events:List[TEDEvent]):Html = {
//    val articleList = events.map{article =>
//      val title = views.html.tedTitle(article)
//      val body = views.html.tedEventBody(article)
//      val leftPane = Article(article.title,article.subtitle.get)
//      val rightPane = Article("","",image=article.imgURL)
//      val prettyTitle = viewStyles.html.splitStyle(leftPane,rightPane)//viewStyles.html.style1(Article(title,"") :: Nil)
//      viewStyles.html.accordion(prettyTitle,body).toString()
//    }
//    new Html(articleList.mkString(""))
//  }
//
//  def sponsors = Action {
////    val calendar = views.html.calendar()
//    val sponsors = views.html.sponsors()
////    val sponsorsIcon = "TULogo.png"
////    val prettySponsors = viewStyles.html.style1(Article("<a href=\"https://new.trinity.edu\">Trinity University</a>","",image=Some(sponsorsIcon)))
//    Ok(views.html.main("Sponsors", sponsors))
//  }
//
//  def aboutTED = Action {
//    // val aboutTed = views.html.aboutTedContent()
//    val aboutTed = viewStyles.html.style4(util.aboutTedText.content)
//    Ok(views.html.main("About TEDx", aboutTed))
//  }
//
//  def contact = Action {
//    val people = TeamMembers()
//    val icons = people.map(p => new Html(p.imgLink))
//    val peopleArticles = people.map(p => Article(p.name,p.description,Some(p.email + " | " + p.classDescription),Some(p.imgLink)))
//    val htmlList = peopleArticles.map(i => viewStyles.html.style1(i))
//    Ok(views.html.main("Our Team",htmlList.mkString(",")))
//  }
//
//  def contact2 = Action {
//    val people = TeamMembers()
//    val tiles = people.map{person =>
//      val article1 = Article(person.role,"",None,Some(person.imgLink))
//      val article2 = Article(person.name,person.description,Some(person.classDescription))
//      viewStyles.html.splitStyle(article1,article2,id=person.emailName)
//    }
//    Ok(views.html.main("Our Team",tiles.mkString(",")))
//  }
//
//  def newsfeed = Action {
//    val newsButton = new Html("""<a href="/"><input type="button" value="Subscribe to Newsletter"></input></a>""")
//    Ok(views.html.main("News Feed",models.Newsletter.NewsfeedList,Some(newsButton)))
//  }
//
//  def getAPost(name:String) = Action {
//    val newsArticle = models.Newsletter.getArticle(name)
//    val post = views.html.newsArticle(newsArticle)
//    Ok(views.html.main(newsArticle.title,post,newsArticle.subtitle))
//
//  }
//
//
//  def postEvent = Action {request =>
//    try {
//
//    println("Received Request")
//    println(request.body.asJson)
//    println("Trying to process")
//    val event = request.body.asJson.map {json =>
//      val title = (json \ "title").as[String]
//      val subtitle = (json \ "subtitle").as[String]
//      val speaker = (json \ "speaker").as[String]
//      val description = (json \ "description").as[String]
//      val venue = (json \ "venue").as[String]
//      val date = (json \ "date").as[String]
//      val time = (json \ "time").as[String]
//      val numSeats = (json \ "numSeats").as[String]
//      val mediaLink = (json \ "mediaLink").as[String]
//      val event = TEDEvent(title,subtitle,speaker,description,venue,date,time,numSeats,mediaLink)
//      event
//    }
//    println("About to add")
//    val eventGotten = event.get
//    println("Called get")
//    addEvent(eventGotten)
//    println("Successfully added")
//    Ok
//    }
//    catch {
//      case e:Throwable => {
//        println("Failed to post correctly")
//        println("Error Message: " + e.toString())
//        null
//      }
//    }
//  }
//
//  def postNewsfeed = Action {request =>
//    try {
//      println("Received Request")
//      println(request.body.asJson)
//      val event = request.body.asJson.map{json =>
//        val title = (json \ "title").as[String]
//        val subtitle = (json \ "subtitle").as[String]
//        val author = (json \ "author").as[String]
//        val body = (json \ "body").as[String]
//        val mediaLink = (json \ "mediaLink").as[String]
//        val time = java.time.LocalDateTime.now()
//        val members = TeamMembers()
//        val user = members.filter(_.email == author).head
//        val post = Newsletter(title,subtitle,user,body,time,mediaLink)
//        Newsletter.addNewsletter(post)
//      }
//      Ok
//    }
//    catch {
//      case e:Throwable => {
//        println("Failed to post correctly")
//        println("Error Message: " + e.toString())
//        null
//      }
//    }
//  }
//
//
//  val eventForm = Form(
//		  mapping(
//				  "title" -> text,
//				  "subtitle" -> text,
//				  "speaker" -> text,
//				  "description" -> text,
//				  "venue" -> text,
//				  "date" -> text,
//				  "time" -> text,
//				  "numSeats" -> text,
//				  "mediaLink" -> text)(TEDEvent.apply)(TEDEvent.unapply))
//
//
//  private def addEvent(event: TEDEvent) = {
//    println("Made it into the event")
//    val date = event.date.atZone(ZoneId.of("America/Chicago"))
//    val epochsec = date.getLong(ChronoField.INSTANT_SECONDS) * 1000
//    val sqldate = new Date(epochsec)
//    val sqltime = new Time(epochsec)
//    val id = 0
//      val eventDesc = EventDescriptionsRow(
//        id,
//        event.title,
//        event.subtitle,
//        event.speakers.mkString(","),
//        event.description,
//        event.venue,
//        sqldate,
//        sqltime,
//        event.maxSeats,
//        0,
//        event.imgURL)
//    val finalQuery =  db.run(EventDescriptions += eventDesc)
//    println("Just passed db.run")
//    finalQuery.map{i =>
//      println("Query Update Returned " + i)
//      updateModel()
//  }
//}
//
//  private def updateModel() = {
//    val events = db.run(EventDescriptions.take(100).result)
//    events.map{seq =>
//      TEDEventList.updateList(seq)
//    }
//  }
//
//  private def clean(str:String):String = {
//    val specials = Array(("%2F" -> "/"),("%3A" -> ":"),( "%2E" -> "."))
//    var s = str
//    specials.foreach{subst =>
//      s = s.replaceAll(subst._1, subst._2)
//    }
//    s
//  }
//
//}
