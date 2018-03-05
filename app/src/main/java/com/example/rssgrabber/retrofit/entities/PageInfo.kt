package com.example.rssgrabber.retrofit.entities

import pl.droidsonroids.jspoon.annotation.Selector

class PageInfo {
    @Selector("#jobs_list_title") var jobsTitle: String? = null
    @Selector("meta[property=og:title]", attr = "content") var title: String? = null
    @Selector("meta[property=og:description]", attr = "content") var description: String? = null
    @Selector("meta[property=og:image]", attr = "content") var image: String? = null
    @Selector(".input .checkbox_list .checkbox_single label") var divisions: List<String>? = null
}