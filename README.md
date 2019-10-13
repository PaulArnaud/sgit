# sgit

***Copy of GIT with Scala language***  
Date : _5 -> 20 October 2019_  
by **Paul ARNAUD**  
# Instructions  

The instructions for compiling and testing the project are as follows:

* clone this github repository  
This requires that you have installed version 1.3.2 of sbt and 2.13.0 of scala
* launch the command 'sbt assembly'

You will then find an executable file in "/target/scala-2.13/" which has the name sgit.
To be able to use this executable anywhere on your computer, you can export the path to the "PATH/target/scala-2.13/" clone

## Architecture  

In this section, you should
explain what is the architecture of your application and your conception choices.
explicitly have two subsections talking about the pros and the cons of your architecture
add at least one figure to describe your application

## Test strategy  

I have tried to test each function

## Post Mortem  

* What went right
Nothing ?
* What went wrong
Test classes was late.... middle of the project
* Lessons learned
  * How to build a complex project with many libraries
  * How to set up test
sbt test
funsuite
  * How to make complex analyses about test
sonarQube  