# HTML Extractor
Given a URL and output file as parameters. Output the features of the web
page into passed file


### Dependencies
* jsoup
* apache-commons library 
* apache log4j

### Build
mvn package

### Running
java -jar [jar-filename] [url] [output file name]



#####Definition of Sequence:
* numbers are considered capitalized
* punctuation to be removed = all characters that are not letters or numbers
* sequence = two or more words that have the first letter of each letter capitalized
* word = characters of length greater than two seperated by white space


