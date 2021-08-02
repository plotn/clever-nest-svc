package com.plotn.cleverNest.controller

import com.plotn.cleverNest.model.db.NewsRecord
import org.springframework.core.env.Environment
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
import org.springframework.beans.factory.annotation.Autowired
import java.net.URI
import java.nio.charset.Charset

@RestController
@RequestMapping("/news")
class NewsController {

    @Autowired
    private lateinit var env: Environment

    private val logger = LoggerFactory.getLogger(javaClass)
    fun log(s: String) = logger.info(s)

    @JvmOverloads
    fun ByteArray.toHexString(separator: CharSequence = " ",  prefix: CharSequence = "[",  postfix: CharSequence = "]") =
        this.joinToString(separator, prefix, postfix) {
            "%" + String.format("%02X", it)
    }

    @GetMapping("/getRandomNewsMeduza", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Scheduled(fixedRate = 100000) // 5/3 min
    fun getRandomNewsMeduza(): NewsRecord {
        log("fetching news record")
        val nr = NewsRecord("")
        val meduzaUrl = env.getProperty("news.meduzaUrl") ?: ""
        if (meduzaUrl.isNullOrBlank()) {
            log("meduzaUrl is null")
        }
        val clockAddr = env.getProperty("news.clockAddr");
        log("url is: $meduzaUrl")
        val doc: Document = Jsoup.connect(meduzaUrl).get()
        val titles: Elements = doc.select("title")
        val listNews = ArrayList<String>()
        for (title: Element in titles) {
            listNews.add(title.text())
        }
        if (listNews.size>0) listNews.removeAt(0)
        if (listNews.size>0) {
            var rnd = Random.nextInt(listNews.size)
            nr.newsText = listNews[rnd]
            log("news text got: ${nr.newsText}")
            val b: ByteArray = nr.newsText.toLowerCase().toByteArray(Charset.forName("CP1251"))
            var updString = b.toHexString("","","").replace("%A0", "%20").
                replace("%AB", "%22").replace("%BB", "%22")
            nr.newsText = updString
            //nr.newsText = String(b, Charset.forName("CP1251"))
            log("news text got updated: ${nr.newsText}")
        }
        val restTemplate = RestTemplate()
        val uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host(clockAddr).path("/mess")
                //.queryParam("text", nr.newsText.replace("\\s+".toRegex(), "_"))
                .queryParam("text", "12345678901234567890")
                .queryParam("delayed","1")
                .build()
        var s = uriComponents.toUriString().replace("12345678901234567890", nr.newsText)
        log("news text got updated 2: $s")
        val uri = URI(s)
        /*val resp =*/ restTemplate.getForEntity(uri, String::class.java)
        //var sBody = resp.body
        return nr
    }
}
