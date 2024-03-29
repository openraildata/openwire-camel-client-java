National Rail Open Data Java Example
====================================

This repository contains two Java 8 examples of how to use the Darwin v16 messages
from the National Rail Open Data platform, located at the following URL:

* https://opendata.nationalrail.co.uk/

To use this service, you will need to sign up for a free account and subscribe
to the 'Darwin' feed.

Configuration
-------------

Edit `src/main/resources/application.properties`, uncomment and set the following fields:

* **account name** is the email address used to log in to the portal
* **username** and **password** are listed on the 'My Feeds' page of the portal - the username will start with 'DARWIN'
* **hostname** is the messaging host listed on the 'My Feeds' page of the portal

To customise the JMS Client ID sent to the ActiveMQ server, change the definition
of `this.clientId` in the constructor method for `DarwinClientRouteBuilder`. You
do not need to change this unless you will have more than one client subscribing
to the topic.

Generating classes
------------------

The messages are produced in XML format, and a good way to consume them is by
using generated classes with [JAXB](https://github.com/mojohaus/jaxb2-maven-plugin). 

Using Maven, run the following command:

`mvn jaxb2:xjc`

This will generate classes in `target/classes`.  You may need to mark the
`generated-sources` directory in your IDE as a generated sources root.

Running the code
----------------

The code has been written on Java 8 with [Apache Maven](https://maven.apache.org/).
Other versions of Java have not been tested.

Running the `DarwinClient` class will show the raw message body and print the
timestamp from the parse XML through the classes generated by JAXB.

Support
-------

For support and questions with using Darwin, please use the forum at the
following URL:
 
 * https://groups.google.com/group/openraildata-talk