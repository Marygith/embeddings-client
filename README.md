# Service for providing static entry point to embeddings retrieval project

This project provides endpoint **/embeddings/{id}**, all incoming requests are forwarded to the available master(via RR algo).
This functionality is enabled via Apache Curator library. 

## Requirements

This project requires java 21 and running zookeeper(by defult address - localhost:2181)
