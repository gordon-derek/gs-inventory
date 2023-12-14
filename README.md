# GS-Inventory

## Problem Statement
Build a Simple Inventory Management System that can handle Bin Maintenance 
and store what inventory is contained within each bin.

* Items will come from a different internal team.
* Bins will come from a 3rd Party system
* Items and Bins know nothing of the others catalog.

## Decisions
I went with a CQRS and event-sourced approach as the way the problem was
being described in the document really seemed like something that could flow
easily.  I also liked the separation of the business logic into an aggregate,
which made testing a bit more organized, although more tests should really exist.

How I would want to bring new bins over to the system needs more thought and fleshing
out.  The goal was that I would make an external api call to SmartRack and based on the
response from the service, do my checking and updating of the current bins and add any new ones.

To kind of mimic this I made a pretty gross method in the bin controller to seed the initial batch of
bins.

## Things I Would Do Differently
This was my first delve into CQRS and Event Sourcing within Java, NestJs does this a bit differently.  There's some nitty
gritty details that I still want to fix that come up just from lack of experience working
with it. The biggest one is the ResponseEntities with working with async code in Java.
I need to iron out the global exception handlers and set up proper http exceptions.  I would have started
with the exception framework so I wasn't trying to wire that in down the road and fighting with any
decisions I previously made.

I also should have taken a step back and decided how I wanted to present data for testing purposed with a JSON store.
Because this is event sourcing, we couldn't just have a database that could be modified or played with as that would ruin
the parity between the event store and the repository. Currently the repository is in memory, I think even locally it'd
have to be a standard dbms, it wasn't until the end that I thought about how I'm missing a mark not having a written json
file for data to be inspected.

## Build and Run Instructions
This assumes you already have docker installed

After cloning this repo:
* Start Dependency (Axon Server Eventstore)
  * Run `docker-compose up`
  * If first time
    * Navigate to http://localhost:8024
    * Complete setup of axon server with default values
* Build the project
  * `./gradlew build`
* Test the project
  * `./gradlew test`
* Start the server
  * `./gradlew bootRun`

## Communicating with the Server
There is a basic swagger set up to outline all of the routes you can use for both bins and items.
Running the server for the first time you should hit the `bins/seed` endpoint to get some basic bins
set up for you to play with. If you'd like to add more you can modify the `seed.json` file located under `db/bin`.
This cannot run more then once without errors(Fast follow item).

Swagger: http://localhost:3000/swagger-ui/index.html#/

