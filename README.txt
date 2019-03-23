Evaluation of Semantic Engines; doing queries against user-agent database file (csv file)

Technologies used:

Windows 10 x64
Java JDK 8 (1.8.1_181)
Eclipse Photon (4.7.3)

Solr (7.7.1)
ElasticSearch (6.6.1)
Lucene (4.0.0)
Browscap (1.2.23)
jedis (3.0.1)


SpringBoot 2.0.0/ElasticSearch 5.6.11
Redis for Windows x64 v 3.2.100


Projects:

ParserBrowscap ==> Parser/Query user_agent database using Browscap library
ParserUserElastic ==> Parser/Query user_agent database using ElasticSearch Java client library
ParserUserLucene ==> Parser/Query user_agent database using Lucene library 
ParserUserSolr ==> Parser/Query user_agent database using Solr Java client library
ParserUserRedis ==> Parser/Query user_agent database using Redis Jedis Java client library
SpringBootFilter ==> SpringBoot + interceptor + ElasticSearch browser user_agent evaluation.



