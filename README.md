## Mutation Testing ##
Mutation Testing is a type of software testing where we mutate/change certain statements in the source code for determining the effectiveness of the test set in finding errors and isolating the deviations. If your test set fails against the mutant code (it doesn’t give the same output as with the original code), then the mutation is killed. if your test set passes, then the mutation lives on. The quality of your tests can be gauged from the percentage of mutations killed. The changes in mutant program are kept extremely small, so it does not affect the overall objective of the program. This method is also called as Fault based testing strategy as it involves creating fault in the program

Existing Mutation Testing Tools: 
MuJava : https://cs.gmu.edu/~offutt/mujava/
Pitest : http://pitest.org/

We have observed the way the existing mutation testing systems work to build our own mutation testing tool

## Tasks being performed ##
#### 1)Save Execution Trace:
#### 2)Creation of the Mutation Matrix:
#### 3)Deciding which mutation to apply:
#### 4)Apply mutation using AST and return the modified code string:
#### 5)Replacing method body using Javassist:
#### 6)Comparing Traces

## Classes to be run: ##
#### -Run tianma.learn.ds.Launcher.UsingSingleThreadExecutor.java
#### -Run tianma.learn.ds.Launcher.Launcher.java

### 1) Save Execution Trace: ###
In our project, we collect the instrumented byte code from Homework 2. 
We use reflection on the instrumented file and we obtain the byte code of the instrumented file.
We run our instrumented byte code against the test cases specified in the Config file.
For doing this, Run UsingSingleThreadExecutor.java
Run this file in order to obtain the ExecutionTrace of the original file.
In the UsingSingleThreadExecutor, we iterate through the test cases in the Config file corresponding to the input program file and we execute the tasks specified in the TracePrint using the ExecutorService’s Executors.newSingleThreadExecutor() instance.
This will run each of the threads consecutively and the execution traces of each of the files are printed in files- ExecutionTrace_0, ExecutionTrace_1, ExecutionTrace_2.
Each file corresponds to the execution trace corresponding to the test cases specified by the Config file.


### 2) Creation of the Mutation Matrix: ###
Now, start the Launcher by running Launcher.java.
Using the ExecutionTraces as input, the Mutator.java iterates through the lines and creates the hash map with 
####  KEY- Operator list 
####  VALUE - the concatenated string containing Line number, method name, statement type, expression and the list of operators that the expression contains 

### 3) Deciding which mutation to apply: ###
For each of the operator in the KEY’s operator list, we create the final multi map that contains 
#### KEY- Numerical Value starting from zero and auto incrementing for each entry in the multimap.
#### VALUE- list of string arrays containing all the mutations possible for each operator corresponding to the expressions and the statement type.
##### Thus the Mutator provides the final hash map to the Launcher

### 4) Apply mutation using AST and return the modified code string: ###
Now that the control is returned to the Launcher, the launcher calls asMutate() method that will use the Hash map as input.
We also obtain the Abstract Syntax Tree of the Instrumented file and we go through the nodes of the AST and visit the statements and expressions using the Visitor Design Pattern.
The method iterates through the hash map for each entry and checks for each statement type and further, using statement.getExpression(), we obtain the expression and replace the existing operator by the mutation that was decided in the Mutation Matrix.
#### For example, a “==“ would be replaced by a “!=“
The asMutate() method returns the modified Method’s Code Strings which will later be used to create a new CtMethod Object. This Modified Method Code string is also printed to the Console.


### 5) Replacing method body using Javassist:###
The control is again returned to the main of the Launcher again and from here, the ReplaceMethodBody.java’s, createMutation1() is called which uses JAVASSIST, which will replace the old method of the the program with the newly modified Method’s Code Strings by creating a new CtMethod object using CtNewMethod.make().
The ExecutorService now executes MutationThread to generate mutations of the Input original Instrumented Java class.

The new method is written to the CtClass and the updated CtClass is converted to Class type and it is loaded to the Classloader. 
The updated class’s byte code is obtained and the method is invoked by passing the same arguments from the Config file that was initially passed to get the initial Execution Traces.
This will build MutationTrace files containing the instrumented outputs of each of the Mutated Programs.

### 6) Comparing Traces ###
Once the Mutation Trace files have been created, the compareTraces() method compares each of the MutationTraces against the ExecutionTrace created for the corresponding test cases from the Config File. 
#### If the traces are the same,then it means that mutant has survived and the test cases haven't killed the mutant. Result is printed to Console.
##### Bad news: SAME TRACES- Mutant has survived!
#### If the traces are different, then it means that the mutant has been killed by the test cases. Result is printed to Console.
##### Good news: DIFFERENT TRACES -Mutant is killed!
#### If the Mutation results in an exception, the resulting exception is printed out to the MutationTrace file.




