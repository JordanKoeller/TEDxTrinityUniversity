package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = EventDescriptions.schema ++ NewsletterPost.schema ++ WhitelistedUsers.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table EventDescriptions
   *  @param eventId Database column event_id SqlType(int4)
   *  @param title Database column title SqlType(varchar), Length(1000,true)
   *  @param subtitle Database column subtitle SqlType(varchar), Length(1000,true), Default(None)
   *  @param speaker Database column speaker SqlType(varchar), Length(1000,true)
   *  @param description Database column description SqlType(text)
   *  @param venue Database column venue SqlType(varchar), Length(1000,true)
   *  @param eventDate Database column event_date SqlType(date)
   *  @param eventTime Database column event_time SqlType(time)
   *  @param maxSeats Database column max_seats SqlType(int4)
   *  @param takenSeats Database column taken_seats SqlType(int4)
   *  @param mediaLink Database column media_link SqlType(varchar), Length(1000,true), Default(None) */
  case class EventDescriptionsRow(eventId: Int, title: String, subtitle: Option[String] = None, speaker: String, description: String, venue: String, eventDate: java.sql.Date, eventTime: java.sql.Time, maxSeats: Int, takenSeats: Int, mediaLink: Option[String] = None)
  /** GetResult implicit for fetching EventDescriptionsRow objects using plain SQL queries */
  implicit def GetResultEventDescriptionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Date], e4: GR[java.sql.Time]): GR[EventDescriptionsRow] = GR{
    prs => import prs._
    EventDescriptionsRow.tupled((<<[Int], <<[String], <<?[String], <<[String], <<[String], <<[String], <<[java.sql.Date], <<[java.sql.Time], <<[Int], <<[Int], <<?[String]))
  }
  /** Table description of table event_descriptions. Objects of this class serve as prototypes for rows in queries. */
  class EventDescriptions(_tableTag: Tag) extends profile.api.Table[EventDescriptionsRow](_tableTag, "event_descriptions") {
    def * = (eventId, title, subtitle, speaker, description, venue, eventDate, eventTime, maxSeats, takenSeats, mediaLink) <> (EventDescriptionsRow.tupled, EventDescriptionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(eventId), Rep.Some(title), subtitle, Rep.Some(speaker), Rep.Some(description), Rep.Some(venue), Rep.Some(eventDate), Rep.Some(eventTime), Rep.Some(maxSeats), Rep.Some(takenSeats), mediaLink).shaped.<>({r=>import r._; _1.map(_=> EventDescriptionsRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column event_id SqlType(int4) */
    val eventId: Rep[Int] = column[Int]("event_id")
    /** Database column title SqlType(varchar), Length(1000,true) */
    val title: Rep[String] = column[String]("title", O.Length(1000,varying=true))
    /** Database column subtitle SqlType(varchar), Length(1000,true), Default(None) */
    val subtitle: Rep[Option[String]] = column[Option[String]]("subtitle", O.Length(1000,varying=true), O.Default(None))
    /** Database column speaker SqlType(varchar), Length(1000,true) */
    val speaker: Rep[String] = column[String]("speaker", O.Length(1000,varying=true))
    /** Database column description SqlType(text) */
    val description: Rep[String] = column[String]("description")
    /** Database column venue SqlType(varchar), Length(1000,true) */
    val venue: Rep[String] = column[String]("venue", O.Length(1000,varying=true))
    /** Database column event_date SqlType(date) */
    val eventDate: Rep[java.sql.Date] = column[java.sql.Date]("event_date")
    /** Database column event_time SqlType(time) */
    val eventTime: Rep[java.sql.Time] = column[java.sql.Time]("event_time")
    /** Database column max_seats SqlType(int4) */
    val maxSeats: Rep[Int] = column[Int]("max_seats")
    /** Database column taken_seats SqlType(int4) */
    val takenSeats: Rep[Int] = column[Int]("taken_seats")
    /** Database column media_link SqlType(varchar), Length(1000,true), Default(None) */
    val mediaLink: Rep[Option[String]] = column[Option[String]]("media_link", O.Length(1000,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table EventDescriptions */
  lazy val EventDescriptions = new TableQuery(tag => new EventDescriptions(tag))

  /** Entity class storing rows of table NewsletterPost
   *  @param postId Database column post_id SqlType(int4)
   *  @param postDate Database column post_date SqlType(date)
   *  @param postUserId Database column post_user_id SqlType(int4)
   *  @param title Database column title SqlType(varchar), Length(1000,true)
   *  @param subtitle Database column subtitle SqlType(varchar), Length(1000,true) */
  case class NewsletterPostRow(postId: Int, postDate: java.sql.Date, postUserId: Int, title: String, subtitle: String)
  /** GetResult implicit for fetching NewsletterPostRow objects using plain SQL queries */
  implicit def GetResultNewsletterPostRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[String]): GR[NewsletterPostRow] = GR{
    prs => import prs._
    NewsletterPostRow.tupled((<<[Int], <<[java.sql.Date], <<[Int], <<[String], <<[String]))
  }
  /** Table description of table newsletter_post. Objects of this class serve as prototypes for rows in queries. */
  class NewsletterPost(_tableTag: Tag) extends profile.api.Table[NewsletterPostRow](_tableTag, "newsletter_post") {
    def * = (postId, postDate, postUserId, title, subtitle) <> (NewsletterPostRow.tupled, NewsletterPostRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(postId), Rep.Some(postDate), Rep.Some(postUserId), Rep.Some(title), Rep.Some(subtitle)).shaped.<>({r=>import r._; _1.map(_=> NewsletterPostRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column post_id SqlType(int4) */
    val postId: Rep[Int] = column[Int]("post_id")
    /** Database column post_date SqlType(date) */
    val postDate: Rep[java.sql.Date] = column[java.sql.Date]("post_date")
    /** Database column post_user_id SqlType(int4) */
    val postUserId: Rep[Int] = column[Int]("post_user_id")
    /** Database column title SqlType(varchar), Length(1000,true) */
    val title: Rep[String] = column[String]("title", O.Length(1000,varying=true))
    /** Database column subtitle SqlType(varchar), Length(1000,true) */
    val subtitle: Rep[String] = column[String]("subtitle", O.Length(1000,varying=true))
  }
  /** Collection-like TableQuery object for table NewsletterPost */
  lazy val NewsletterPost = new TableQuery(tag => new NewsletterPost(tag))

  /** Entity class storing rows of table WhitelistedUsers
   *  @param userId Database column user_id SqlType(int4), PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(100,true)
   *  @param email Database column email SqlType(varchar), Length(40,true) */
  case class WhitelistedUsersRow(userId: Int, username: String, email: String)
  /** GetResult implicit for fetching WhitelistedUsersRow objects using plain SQL queries */
  implicit def GetResultWhitelistedUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[WhitelistedUsersRow] = GR{
    prs => import prs._
    WhitelistedUsersRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table whitelisted_users. Objects of this class serve as prototypes for rows in queries. */
  class WhitelistedUsers(_tableTag: Tag) extends profile.api.Table[WhitelistedUsersRow](_tableTag, "whitelisted_users") {
    def * = (userId, username, email) <> (WhitelistedUsersRow.tupled, WhitelistedUsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(username), Rep.Some(email)).shaped.<>({r=>import r._; _1.map(_=> WhitelistedUsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(int4), PrimaryKey */
    val userId: Rep[Int] = column[Int]("user_id", O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(100,true) */
    val username: Rep[String] = column[String]("username", O.Length(100,varying=true))
    /** Database column email SqlType(varchar), Length(40,true) */
    val email: Rep[String] = column[String]("email", O.Length(40,varying=true))
  }
  /** Collection-like TableQuery object for table WhitelistedUsers */
  lazy val WhitelistedUsers = new TableQuery(tag => new WhitelistedUsers(tag))
}
