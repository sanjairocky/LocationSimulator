Application Name : Location Simulator

problem statement : Given two locations - A & B. You are provided with latitude & longitude of them (short form
LatLngs). You need to calculate ‘real’ points on the road that connects A & B. You need to use data from
google directions API .

Sample Input: LatLngs for point A and point B.
A is 12.93175, 77.62872 and B is 12.92662, 77.63696

Sample Output: LatLngs at a constant distance interval of 50 m between A & B on the road.
12.93175, 77.62872
12.93166, 77.62852
12.93125, 77.62870
.
.
12.92713, 77.63719
12.92668, 77.63717
12.92662, 77.63696

Tech Stack : springboot, google-api-services, Coordinates Geomentry references from StackOverFlow.

Language : Java

Compatible Version : 1.8

Folder Structure :

    -   codes -> Error code's
    -   domain ->  POJO's contains basic logic bounded around it.
        - * LatLng.java -> Contains setter's & getter's as well as functions 
            * find distance between two points
            * find bearing of two points
            * Generate a point by moving some distance
    -   Exception ->  Specific RuntimeException class
    -   helper
        -  * GeoApiHelper.java ->  Deals with google's direction API to get the coordinates with fallback when cert error from google library.
    -   manager
        -  * LocationSimulatorManager.java -> Kinda like a domain in MVC. Manages the all the transaction steps needed to collect Coordinates.
    -   service
        -  LocationSimulatorService.java -> Service layer delegation
    -   util
        -  * IntervalCalculator.java -> It is where actual computation of coordinates happens.
        -  * GeoApiContextFallBack.java -> Is a Fallback feature. I used to get a cert exception from google-api-services library.

Note : 

    - (*) files contains the actual functional logic.
    - Please refer screenshots folder for output ( i.e output are captured from https://mobisoftinfotech.com/tools/plot-multiple-points-on-map/)
    - Since i stick with the logic's. haven't focused on writing Junit & Integeration TC's.


To Execute: please use following cmd:

    - Navigate to root directory  & install dependencies

        mvnw install

    - Run the application

        mvnw spring-boot:run

                or
        
        mvnw clean package && java -jar cd  "target\LocationSimulator-0.0.1-SNAPSHOT.jar"



