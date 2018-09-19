package models

import java.time.LocalDateTime

import controllers.routes
import play.twirl.api.Html

import models.TeamMembers.WebCoordinator

import util.Article
import util._

class NewsletterPost(
  val title: String,
  val subtitle: Option[String],
  val author: TeamMember,
  val description: String,
  val date: LocalDateTime,
  val imgURL:String) extends Post {

  def time: String = {
    val hr = date.getHour
    val min = date.getMinute
    val hrFormatted = hr % 12
    val amPm = if (hr >= 12) "PM" else "AM"
    val minString = if (min < 10) "0" + min else min
    val hrString = if (hrFormatted == 0) "12" else hrFormatted
    s"$hrString:$minString $amPm"
  }
  
  def dateTime:String = {
    val day = date.getDayOfMonth
    val month = date.getMonthValue
    val yr = date.getYear
    s"$month/$day/$yr" + " | " + time
  }
  
  def nameLinkString:String = {
    routes.Application.contact2().url + "#"+author.emailName
  }
  
  def articleLinkString:String = {
    println(title)
//    title.toCharArray().filter{p=>
//      Newsletter.alphabet.contains(p)
//    }
    val cleaned = title.toCharArray().filter{c =>
      var found = false
      for (cc <- Newsletter.alphabet.toCharArray()) {
        if (cc == c) found = true
      }
      found
    }.mkString("")
//    println(cleaned)
//    val ret = cleaned.split(" ").mkString("_")
    println(cleaned)
    cleaned
//    title
  }
  
  def article:Html = {
    
    ???
  }


}


object Newsletter {
  
  private val posts = collection.mutable.ArrayBuffer[NewsletterPost]()
  val alphabet = """ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"""
  
  def apply(title:String,
      subtitle:String,
      author:TeamMember,
      description:String,
      date:LocalDateTime,
      url:String):NewsletterPost = {
    val st = if (subtitle != "") Some(subtitle) else None
    new NewsletterPost(title,st,author,description,date,url)
  }

  def NewsfeedList = {
    val newslist: List[NewsletterPost] = news1 :: Nil//posts.toList

    val titles = newslist.map { article =>
      val article2 = Article("", "",None, Some(article.imgURL))
      val article1 = Article(article.title, article.subtitle.get,Some(article.author.name))
      val articleLink = "Feed/"+article.articleLinkString
      viewStyles.html.splitStyle(article2, article1, link = Some(articleLink))
    }
    println("Right!?")
    
    titles.mkString("")

  }
  
  def getArticle(name:String):NewsletterPost = {
    val post = posts.filter(_.articleLinkString == name)
    post.head
  }
  
  def addNewsletter(letter:NewsletterPost) = {
    println("Added Newsleter")
    posts.append(letter)
  }
  
  val news1 = new NewsletterPost(
      "Newsletter 1",
      Some("By studying gravitationally lensed quasars, astrophysicists have the potential to learn about dark matter distributions in the universe along with the size and composition of distant quasars to orders of magnitude of higher precision than through direct observation. In order to understand these systems, however, we must build a computational model."),
      WebCoordinator,
      "This is a sample web post, supposed to be by Jordan Koeller",
      java.time.LocalDateTime.now(java.time.Clock.systemUTC()),
      "https://i.pinimg.com/736x/f5/df/ff/f5dfffc707db37ceaaad9c5c7018bdcd--artsy-pics-john-green.jpg")
  posts.+=(news1)
}