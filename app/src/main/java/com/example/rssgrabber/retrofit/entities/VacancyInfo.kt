package com.example.rssgrabber.retrofit.entities

import pl.droidsonroids.jspoon.annotation.Selector

class VacancyInfo {
    @Selector(".job_show_header .title") var vacancyTitle: String? = null
    @Selector(".job_show_header .skills .skill") var skils: List<String>? = null
    @Selector(".job_show_header .body_meta .date") var date: String? = null
    @Selector(".job_show_header .body_meta .views") var views: String? = null
    @Selector(".job_show_header .footer_meta .salary") var salary: String? = null
    @Selector(".job_show_header .footer_meta .location") var location: String? = null
    @Selector(".job_show_header .footer_meta .ready_to_remote") var readyToRemote: String? = null
    @Selector(".job_show_description .body .vacancy_description") var vacancyDescription: String? = null
    @Selector(".company_info .logo img", attr = "src") var companyImage: String? = null
    @Selector(".company_info .company_name") var companyName: String? = null
    @Selector(".company_info .company_about") var companyAbout: String? = null
}