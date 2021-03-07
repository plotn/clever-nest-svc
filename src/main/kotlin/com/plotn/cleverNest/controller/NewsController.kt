package com.plotn.cleverNest.controller

import com.plotn.cleverNest.model.db.NewsRecord
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import kotlin.random.Random


@RestController
@RequestMapping("/news")
class NewsController {

    val MEDUZA_URL = "https://meduza.io/rss/all"
    val CLOCK_ADDR = "192.168.0.184"

    private val logger = LoggerFactory.getLogger(javaClass)
    fun log(s: String) = logger.info(s)

    @GetMapping("/getRandomNewsMeduza", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Scheduled(fixedRate = 100000) // 5/3 min
    fun getRandomNewsMeduza(): NewsRecord {
        log("fetching news record")
        val nr = NewsRecord("")
        log("url is: $MEDUZA_URL")
        val doc: Document = Jsoup.connect(MEDUZA_URL).get()
        val titles: Elements = doc.select("title")
        val listNews = ArrayList<String>()
        for (title: Element in titles) {
            listNews.add(title.text())
        }
        if (listNews.size>0) listNews.removeAt(0)
        if (listNews.size>0) {
            var rnd = Random.nextInt(listNews.size)
            nr.newsText = listNews[rnd]
        }
        val restTemplate = RestTemplate()
        val uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host(CLOCK_ADDR).path("/showmessage")
                .queryParam("text", nr.newsText.replace("\\s+".toRegex(), "_"))
                .queryParam("delayed","1")
                .build()
        val resp = restTemplate.getForEntity(uriComponents.toUriString(), String::class.java)
        var sBody = resp.body
        return nr
    }
}
