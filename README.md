# HTML Extractor
Given a URL and output file as parameters. Output the features of the web
page into passed file

Layout: 
* com.bennytran.Client Method
* URL Validator
* Get Links
* Get HTML Tags
* Find Sequences 

### Dependencies
* Maven
* Jsoup
* apache-commons

### Build
mvn package

### Running
java -jar [jar-filename] [url] [output file name]


TODO
* type up assumptions
* setting different properties file
* test test test
* need to set up proper maven build
* figure out how to handle abbreviations in sequence generator, f.e. M&A

Assumptions:
* Sequences
* numbers are considered capitalized
* punctuation is removed and replaced with a space

* Link is defined by a valid URL with HTTP or HTTPS protocol
