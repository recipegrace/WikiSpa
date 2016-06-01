package com.recipegrace.wikispa.mediawiki

import com.recipegrace.wikispa.BasicTest
import com.recipegrace.wikispa.extractors.Categories

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class MediaWikiAccessTest extends BasicTest with MediaWikiAccess{

  test("Alabama page categories test") {

    val page = wikiTitleSearch("Alabama")
    val categories= Categories.extract(page).get._2


    categories should contain ("Southern United States")
  }

}
