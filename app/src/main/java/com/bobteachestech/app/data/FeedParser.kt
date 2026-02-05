package com.bobteachestech.app.data

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

object FeedParser {
    fun parseRss(xml: String): List<FeedItem> {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(xml.reader())

        val items = mutableListOf<FeedItem>()
        var eventType = parser.eventType
        var currentTitle = ""
        var currentLink = ""
        var currentDate = ""
        var insideItem = false

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    "item" -> {
                        insideItem = true
                        currentTitle = ""
                        currentLink = ""
                        currentDate = ""
                    }
                    "title" -> if (insideItem) currentTitle = parser.nextText().trim()
                    "link" -> if (insideItem) currentLink = parser.nextText().trim()
                    "pubDate" -> if (insideItem) currentDate = parser.nextText().trim()
                }
                XmlPullParser.END_TAG -> if (parser.name == "item") {
                    insideItem = false
                    if (currentTitle.isNotBlank() && currentLink.isNotBlank()) {
                        items.add(FeedItem(currentTitle, currentLink, currentDate))
                    }
                }
            }
            eventType = parser.next()
        }

        return items
    }

    fun parseAtom(xml: String): List<FeedItem> {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(xml.reader())

        val items = mutableListOf<FeedItem>()
        var eventType = parser.eventType
        var currentTitle = ""
        var currentLink = ""
        var currentDate = ""
        var insideEntry = false

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    "entry" -> {
                        insideEntry = true
                        currentTitle = ""
                        currentLink = ""
                        currentDate = ""
                    }
                    "title" -> if (insideEntry) currentTitle = parser.nextText().trim()
                    "link" -> if (insideEntry) {
                        val rel = parser.getAttributeValue(null, "rel")
                        val href = parser.getAttributeValue(null, "href")
                        if (rel == null || rel == "alternate") {
                            currentLink = href ?: currentLink
                        }
                    }
                    "published" -> if (insideEntry) currentDate = parser.nextText().trim()
                    "updated" -> if (insideEntry && currentDate.isBlank()) currentDate = parser.nextText().trim()
                }
                XmlPullParser.END_TAG -> if (parser.name == "entry") {
                    insideEntry = false
                    if (currentTitle.isNotBlank() && currentLink.isNotBlank()) {
                        items.add(FeedItem(currentTitle, currentLink, currentDate))
                    }
                }
            }
            eventType = parser.next()
        }

        return items
    }
}
