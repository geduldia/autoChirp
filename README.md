autoChirp Readme
================

## About

<a href="https://autochirp.spinfo.uni-koeln.de">autoChirp</a>
 is a small and easy-to-use webapplication to ease the scheduled publication of your tweets. 

If you're a power-user, upload a bunch of tweets from your desktop or GoogleDrive: Just follow the guidelines describing the creation of a tweet-table or clone the predefined GoogleDrive Spreadsheet, input your data and import the file - et voilà!

Further, autoChirp empowers you to fit more content into your tweets by providing the possibility to automagically convert your longer-than-280-character-tweets into an image.


## Employed technologies
This application is build upon the Spring MVC framework (with its Spring Social Twitter module) and uses Thymeleaf as templating-engine while custom styles are written in SASS. Behind the scenes Heideltime and the TreeTagger dig through Wikipedia-articles to find parsable dates and extract those. 

###  Licenses
All code this project contains is licensed under the Eclipse Public License v1.0 (EPLv1). You should have obtained a copy of the license bundled with the code. All third-party libraries are subject to their respective licences:
* Heideltime is licensed under the GPLv3
* Apache UIMA is licensed under the Apache License 2.0
