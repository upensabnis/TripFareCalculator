# TripFareCalculator
Java application to calculate status of trip and corresponding fare while travelling between various stops. Outcome of the trip
depends on combinations of tap `ON` and `OFF`. The outcome of trip might be one of `COMPLETED`, `INCOMPLETE` or `CANCELED`.

This application reads and writes to `CSV`. It reads 2 CSVs (one about fares between various stops and second about single way trips) to create state in memory.
After processing, it writes output to `trips.csv`.

# Assumptions
--------------
1. Invalid values are skipped. For example, if value of TapType is anything other than `ON` or `OFF`, that corresponding row is skipped.
2. If any issue with parsing csv input, then that particular row is skipped. This might affect the final output of program. For example, if a row with TapType Off skipped then the corresponding result might be INCOMPLETE instead of COMPLETE.
3. Output will not be in same sequence as input. As all rows can be stored in in-memory data structure, the sequence will change.
4. CANCELLED trip case - If tapped on and tapped off at same stop within 2 hours then it is treated as cancelled.
5. COMPLETE trip cases - first, when passenger tap ON and tap OFF at different stops but within 12 hours. Second, when passenger tap ON and tap OFF at same stops within 2 to 12 hours. In second case, passenger will be charged the minimum amount for a trip from that stop to any other stop they could have
   travelled to.
6. INCOMPLETE trip case - first, when passenger tap ON but never tap OFF. Second, when tap OFF happens after 12 hours.
7. There is no case that user tapped `OFF` but did not tap `ON`.
8. Input file is within size to be loaded into memory.
9. Stop ids are consistent across inputs. If there is mismatch, fares for trips starting from some stops could be 0.

# System requirements
---------------------
1. Java 1.8
2. Maven 3.0

# How to build
---------------
This application is built on maven so all standard maven commands should work.
For example, `mvn clean verify` will build and run all tests.

# How to run
-------------
When application is run using maven, automatic tests run and corresponding output file `trips.csv` will be created in the project root folder.
Application also generates jar file in target folder - `TripFareCalculator-1.0-SNAPSHOT.jar`. That jar can be executed from project root folder on command line using `java -jar target/TripFareCalculator-1.0-SNAPSHOT.jar`. Corresponding output file `trips.csv` will be created in the project root folder.