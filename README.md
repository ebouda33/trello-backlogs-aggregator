# trello-backlogs-aggregator

Spring boot application to display a backlog's aggregate.
All the backlogs must have the same columns and these must be configured in the configuration file below. 
You can define which columns are in the sprint. 

You can summarize card values by their complexity. 
To take in account complexities, the card must be declared in Trello like this : 
```
(businessComplexity) card title {consumedComplexity} [totalComplexity]
```

This application need bywan/trello-java-wrapper dependency but, for now, the PR for organization boards is not taken in account.
Therefore, it is needed to use mickaelponsolle/trello-java-wrapper

You have to write a configuration file in the same folder than the application jar. 

The configuration file looks like this : 

```
proxy.host=[blank if not needed]
proxy.port=[blank if not needed]
proxy.user=[blank if not needed]
proxy.password=[blank if not needed]

trello.organisation.id=test
trello.application.key=e7064565756545fez8df15d6fzfd87f5
trello.access.token=3a04878454def84d8e4g0ef4g8f7h7g4hrs4f7hz89fvh94f8dvh4z87grj1g5sj

trello.column.allowed=Ideas, Writing in progress, User stories ready, To do, In progress, Done in sprint, Q&A, Done in production
trello.column.sprint=To do, In progress, Done in sprint

batch.frequency=0 * * * * *
```

To install the application with systemd, create a file trello-backlogs-aggregator.service in /etc/systemd/system like this :

```
[Unit]
Description=Trello Backlogs AggregatoR

[Service]
Type=oneshot
ExecStart=/bin/sh -c "java -Dloader.path=[INSTALLATION_PATH]  -jar [INSTALLATION_PATH]/trello-backlogs-aggregator-0.0.1-SNAPSHOT.jar"

[Install]
WantedBy=multi-user.target

```
