# Kinesis Consumer

Dev
===
* Copy `dev.template.conf` to `dev.conf` under `src/main/resources`
* Set `stream-name` to your kinesis instance
* Set `mongo.uri` and `mongo.db-name` to your mongodb instance
* Tweak `parallelism` and `insert-batch-number` values depending on your system and database instance size

Run
===
`sbt -Dconfig.resource=dev.conf run`