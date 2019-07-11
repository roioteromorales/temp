#Transactions Statistics API
We would like to have a RESTful API for our statistics. The main use case for the API is to calculate realtime statistics for the last 60 seconds of transactions.

The API needs the following endpoints:
 * POST /transactions – called every time a transaction is made.
 * GET /statistics – returns the statistic based of the transactions of the last 60 seconds.
 * DELETE /transactions – deletes all transactions.


##Legend
 * *Time to keep*: 60 seconds - Configurable with 1 variable
 * *Granularity*: 1 second - Configurable with 1 variable
 * *Buckets*: *Time to keep* / *Granularity*. 60/1 = 60

##Problems and solutions
####Problem 1 - Aggregation of Big Decimal to create statistics in O(1).
	
Created **BigDecimalSummaryStatistics** class inspired by Java's [DoubleSummaryStatistics](https://docs.oracle.com/javase/8/docs/api/java/util/DoubleSummaryStatistics.html) to aggregate statistics at O(1) per insert.
####Problem 2 - Keep only N amount of seconds of transactions without scheduled cleanup.

Created *Buckets* for storing SummaryStatistics, one *Bucket* per second, so 60 buckets.
Every *Bucket* contains the aggregation of the statistics of that second and the time it was created.

#####Others: 
Wrapped Java's Instant class into TimeProvider for making easier the testing.

##Architecture (3-tier)
Created 3 layers (presentation, logic and data). Logic layer is the business, should not change and its the core of the application.
This layer has no dependencies from other layers. Other layers depend on it, is protected against changes like changing the presentation
to a Web application or changing the InMemoryRepository to a Database one. 

To achieve this were used:
 * Interfaces - Usage of the interface for the repository instead of the implementation from the logic layer
 * Models per Layer - Models in logic are the core of the app. They should not change if we want to change the presentation,
  therefor the presentation layer contains its own models adapted to the need of the current presentation without interfering 
  in the logic layer.
 * The package structure - to keep files on the layer the belong. Inside the architectures packages is divided per business area/model (transactions, statistics)

##Time complexity
 * Add transaction - O(1) - The *Bucket* for that second is retrieved and the amount is aggregated to the existing statistics using **BigDecimalSummaryStatistics**.
 * Get Statistics - O(1) - The statistics from every *Bucket* are merged into a total one. O(*Buckets*) for this exercise is 60, so constant.
    *   As well statistics were cached to only do this operation once per *Granularity* unit (in this case every 1 second).

##Concurrency
Every time any *Bucket* is reset or used to aggregate we should restrict the access to it so there will be no race conditions. 
The protection was done using Locks, one per *Bucket* so concurrency among other *Buckets* that are not affected can be achieved safely

##Extra Libraries
 * Rest Assured - Preference over Spring default libraries for easiness of usage and more fluent readability