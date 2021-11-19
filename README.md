# Final-Matrix-Encryption-Code
Generalization of Matrix Class:
Depending on the matrix encryption method, certain types of numbers, which comprise the matrices within those methods, are more appropriate than other types. 
If the series of operations within an encryption method are such that none of the numbers ever leave the set of rational numbers then the Fractions Class is the correct choice. Otherwise, the matrix must be able to take irrational numbers so that the encryption process can be completed. 

Initially, the data type Double was the type of number that comprised each matrix. This worked well enough for approximations of rational numbers, although there were some issues with complete accuracy, but this problem got much worse once irrational numbers were introduced. So, what was necessary was to create a separate class of numbers that a matrix could be made of such that accuaracy was 100%. This is where the class AbstractMathObject came in to play.

AbstractMathObject is, as the name suggests, an abstract class that defines all the operations that can be performed on and with numbers abstractly. Those are the simple operations such as add, minus, times, divide, along with the comparison operations, greater than, less than, eqauls, etc. and more complex operations power and mod. Since Double is a primative data type, all the operations in the matrix class were originally written in a traditional manner. Any operation between numbers like "a + b" had to be rewritten as "a.plus(b)" and the same goes for all of the afformentioned operations in the AbstractMathObject class.

To generalize the Matrix class, it needed to be parameterized with a generic datatype that extends from AbstractMathObject. So any class that implements the abstract methods in AbstractMathObject can be used by the Matrix class. Since Fraction and RootFraction do just that, they were able to replace the primative Double datatype which was there originally. 

Doing this had several benefits:
  (1) Improved accuracy by simulating exact rational and root values.
  (2) Allows for flexibility so that one extention of AbstractMathObject can be used over another when it's appropriate to do so.
  (3) Prevents the code from being unnecessarily redundant.
  
These are the reasons why I decided to put this particular code in its own repository rather than adding it as a new branch off of a previous project.
