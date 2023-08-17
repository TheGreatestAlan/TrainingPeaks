This is the inital system for searching on the provided JSON files.

Pull down the code and run the Main class.  It should print the answer to all the questions provided.

I've separated the code into 3 sections

Main - class where the actual questions are answered

Aggregator - class that handles putting together the information from the DbClient interface

DbClient implementations - in this case it's StreamDbClient and JsonDbClient

StreamDbClient handles most the work of searching through the loaded json representations in memory and asking the questions to the data store that are indicated in the DbClient interface.

JsonDbClient is an extension of StreamDbClient that handles loading the Json in as Lists of objects.  

I thought about just loading it into a proper DB, but figure that falls into YAGNI.  If this were to expand though, we'd just need to implement a client that can connect to the database and run the various queries directly against the database instead of handling the relational connections itself.  

I generally tested out all the functions of the implemented interfaces with more simple test data that's built in the tests themselves.
