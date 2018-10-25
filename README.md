This project is the plugion for JMeter 3.1 which provides the functionality to send Sample results data to elasticsearch

Before using create index and doc type in elasticsearch which you are going to use.
Also, create mapping for "@timestamp" field with the following request:

curl -X PUT "http://{es_host}:{es_port}/{index_name}" -H 'Content-Type: application/json' -d'
{
	"mappings": {
		"{doc_type}": {
			"properties": {
				"@timestamp": {
					"type": "date",
					"format": "epoch_millis"
				}
			}
		}
	}
}'

This plugin requires only 2 external jars: org.slf4j and json-20180813 which you can find in lib folder.
Just put them in lib/ext folder of your jmeter with backendListener.jar.

Usage:
-add backendListener.jar to lib/ext of your jmeter.
-add BackendListener to your TestPlan, choose 'com.BackendListener' implementation and configure parameters.
