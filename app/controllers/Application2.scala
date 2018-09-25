//package controllers
//
//import play.api.mvc.ControllerComponents
//import play.api.db.slick.DatabaseConfigProvider
//import play.api.mvc.AbstractController
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//import javax.inject.Inject
//import scala.concurrent.ExecutionContext
//
//
//@Singleton
//class Application2 @Inject() (
//  protected val dbConfigProvider: DatabaseConfigProvider,
//  cc: ControllerComponents)(implicit ec: ExecutionContext)
//  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {
//  import profile.api._
//  
//  val formAccepter = new FormAccepter()
//  
//}