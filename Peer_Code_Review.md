dwy3 zap3 

#Duplicated Code
###My Suggested Changes For David
- Create another method inside step() that returns a Grid object. Call the method withthe cloned grid object in class Simlation and with new Grid object in class WaTorSimulation, then return.

###David's Suggestions
- David suggested to move the methods getInitialStates() and setInitialStates() from classes SegregationSimulation and GameOfLifeSimulation to the parent class Simulation.

#Long Methods

###David's Suggestions
- David suggested to split the method setNewSimulation() in UserInterface in two methods where the second one will get the name of Simulation to be created, create the proper type of simulation and rreturn it.