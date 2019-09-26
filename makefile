# Makefile used to compile all java classes for Assignment 4
# Based on my code from CSC2001F
# Richard Masson

# Assign subdirectory variables

BIN=./bin
SRC=./src
DOC=./doc
BINA=bin

# Accepted data types
.SUFFIXES: .java .class

#general build rule

$(BIN)/%.class: $(SRC)/%.java
	javac $< -cp $(SRC) -d $(BIN)

#
# build rules
#

all: $(BIN)/WordApp.class

$(BIN)/WordApp.class: $(BIN)/Score.class $(BIN)/WordDictionary.class $(BIN)/WordRecord.class $(BIN)/WordPanel.class $(SRC)/WordApp.java

# 
# Remove all class files if "make clean" is called
#
clean:
	del $(BINA)\*.class

docs:
	javadoc -classpath $(BIN) -d $(DOC) $(SRC)/*.java
