# TR Wiktionary Parser

TR-wiktionary-parser is a library created based on [JWKTL] (https://dkpro.github.io/dkpro-jwktl/) to parse [Turkish wiktionary] (https://tr.wiktionary.org/wiki/Ana_Sayfa) data.

TR-wiktionary-parser can parse 4 different wiktionaries total including 3 languages that JWKTL parses (German, English, Russian) + Turkish.

## Dependencies

Project requires Java 8.

Some jar files ara needed to be able to use the library: [Apache Ant] (http://archive.apache.org/dist/ant/source/) {ant.jar}, [Oracle Berkeley DB Java Edition] (http://www.oracle.com/technetwork/products/berkeleydb/downloads/index-098622.html) {je-x.jar}, and [JWKTL] (http://search.maven.org/#search|ga|1|a%3A%22jwktl%22) {jwktl-x.jar}. We currently use the versions apache-ant-1.9.5, je-6.2.31, and jwktl-1.0.2 (it is the one we build from the source code) which are already located under the folder of "lib".

## Usage

These are the steps you need to follow to set up the library:

* First you need to download the [wiktionary data] (http://dumps.wikimedia.org/backup-index-bydb.html). Careful! You must download not the "wiki" ones but "wiktionary" ones.

  After you go to the specified link for downloading wiktionary data (e.g. [tr-wiktionary-data] (https://dumps.wikimedia.org/trwiktionary/20160601/)), you need to download the one covering "Articles, templates, media/file descriptions, and primary meta-pages." for whole coverage.

* Once you have the data (?.xml.bz2), it is suggested to extract it and use the extracted .xml file to parse tha data faster.

* Now that you have tha data, to parse it, first you should create the database. *util/DataParser.java* is the class to perform it. 

  You need to call its method as in DataParser.parseData(*filePath*, *dbPath*, *langKey*).
  It is suggested to use different database folders for wiktionaries in different languages.

* After the database is ready, you are ready to go. There are examples in class *test/DataAccessorTest.java* showing how to get a specified data from the database.
