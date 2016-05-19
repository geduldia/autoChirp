autoChirp Readme
================

### 1. About
#### The initial idea
The first draft of the idea, which turned out as this project, was conceived in the seminar "Digital Humanities und die Informatik der Geisteswissenschaften" at the Universität zu Köln during the fall semester 2016. First thoughts went towards creating a "social media bot", a semi-interactive application that would parse arbitrary sources and create Tweets or Facebook status-updates from the parsed data. While developing this idea within the seminar, Dr. Øyvind Eide and Dr. Jürgen Hermes came forward with a more concrete project, which was derived from the needs of other, real-life users.

It didn't take long until a first prototype of the to be implemented application was ready to be reviewd; no need to say, our critics and us were confident and went along with the project. The basic idea was to create a web-application, that enables the users to input a Tweet, like they would on Twitter, but schedule it instead of publishing it right away. Utilized to keep track of historic developments by actually publishing event details (as Tweets) on their respective dates, autoChirp is imagened to enrichen the academic dialog.

#### Development and implementation
The autoChirp-development was done independently by us two developers and weekly meetings were basis of collaborative bug-fixing and coordination of further tasks. While we both had no prior experiance with neither the Spring MVC framework nor most of the other employed technologies, we managed to adapt well and help each other out. Problems arose within nearly all the different tasks, while some were merely one web-inquest away, others made it necessary to refactor large parts of code. Lastly all those hurdles were conquered and the project is (at least by us developers) regarded as a success story.

Our academic patrons did alot of testing and reviewing and the outcome of those test-runs and use-cases fed back into the development process. During the final phase of the implementation, a public demonstration of the application was held by Dr. Jürgen Hermes for some of the future user and other interested parties. Even though all mayor construction sites were already resolved and the road for finalizing the code was paved, all feedback and feature-requests, that emerged from that presentation, could still be met. As such, we are proud to present to You the possibilities to append images and geolocations to Tweets, even applicable when importing huge sets from TSV-files!

#### Employed technologies
This application is build upon the Spring MVC framework (with its Spring Social Twitter module) and uses Thymeleaf as templating-engine while custom styles are written in SASS. Behind the scenes Heideltime and the TreeTagger dig through Wikipedia-articles to find parsable dates and extract those. The fully documented source code of this application can be obtained from our public GitHub repository.

### 2. Build and deploy
Clone the repository, use the provided Dockerfile to build a Docker image containing all build dependencies and run the image to compile deployable .jar and .war binaries. The .jar binary is a standalone webapp, as it comes withe the default embedded Apache Tomcat webserver. The .war binary is customized to be deployable within a running Jetty setup. If You prefer to compile autoChirp within Your systems native environment make sure to have Maven up and running and furthermore prefer to use the JDK 8.

#### Build instructions
Assuming You have the Docker and GIT clients installed and configured You may run the following commands within Your favorite shell to build the Docker image containing the webapp build environment and consecutively build the webapp:

    AUTOCHIRP_DIR=/tmp/autochirp
    git clone https://github.com/spinfo/autoChirp ${AUTOCHIRP_DIR}
    docker build --tag buildenv/autochirp ${AUTOCHIRP_DIR}
    docker run --volume ${AUTOCHIRP_DIR}/target:/autochirp/target buildenv/autochirp

Without Docker, use Maven from within the repository root to build Your desired target:

    AUTOCHIRP_DIR=/tmp/autochirp
    git clone https://github.com/spinfo/autoChirp ${AUTOCHIRP_DIR}
    cd ${AUTOCHIRP_DIR}
    mvn package war:war

#### Standalone deployment
If You do not want to run a dedicated Jetty, You can still deploy autoChirp using the standalone .jar file. To configure the service, equip the application.properties file with Your desired values, place it in the same directory as the .jar and run the standalone webapp. By default it will listen on the port 8080 of Your localhost and should ideally not be reached from the outside (use a reverse proxy like Nginx to handle SSL/TLS termination).

#### Jetty deployment
To deploy autoChirp within a running Jetty context, build the .war file and use the autochirp.xml file to configure the webapp through Jetty. For more information regarding the Jetty XML configuration syntax please refer to the Jetty documentation.

### 3. Contact
Universität zu Köln
Sprachliche Informationsverarbeitung
Institut für Linguistik
Albertus-Magnus-Platz
D-50931 Köln (Germany)
Tel: (+49) 221 - 470 4170
Fax: (+49) 221 - 470 5193

### 4. Licenses
All code this project contains is licensed under the Eclipse Public License v1.0 (EPLv1). You should have obtained a copy of the license bundled with the code. All third-party libraries are subject to their respective licences:
* Heideltime is licensed under the GPLv3
* Apache UIMA is licensed under the Apache License 2.0
