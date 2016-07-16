INC_LIST=-I./inc \
	-I/usr/local/mysql/include/mysql/

OBJ=	obj/spBaseTree.o \
	obj/spCiscoParser.o \
	obj/spParser.o \
	obj/spUtilities.o \
	obj/spParserMain.o \
	obj/spConfigDBM.o \
	obj/spConstants.o \
	obj/spBaseTreeManager.o \
	obj/spJuniperParser.o

MYSQL_LIB_DIR_Linux_FC14=/usr/local/mysql/lib/mysql

bin/spConfigParser: $(OBJ)
	g++ -m64 -Wall -g $(OBJ) -o bin/spConfigParser -L$(MYSQL_LIB_DIR_Linux_FC14) -lmysqlclient

obj/spBaseTree.o: src/spBaseTree.cpp
	g++ -m64 -Wall -g -c src/spBaseTree.cpp -o obj/spBaseTree.o -I inc/ $(INC_LIST)
obj/spCiscoParser.o: src/spCiscoParser.cpp
	g++ -m64 -Wall -g -c src/spCiscoParser.cpp -o obj/spCiscoParser.o -I inc/ $(INC_LIST)
obj/spParser.o: src/spParser.cpp
	g++ -m64 -Wall -g -c src/spParser.cpp -o obj/spParser.o -I inc/ $(INC_LIST)
obj/spUtilities.o: src/spUtilities.cpp
	g++ -m64 -Wall -g -c src/spUtilities.cpp -o obj/spUtilities.o -I inc/ $(INC_LIST)
obj/spParserMain.o: src/spParserMain.cpp
	g++ -m64 -Wall -g -c src/spParserMain.cpp -o obj/spParserMain.o -I inc/ $(INC_LIST)
obj/spConfigDBM.o: src/spConfigDBM.cpp
	g++ -m64 -Wall -g -c src/spConfigDBM.cpp -o obj/spConfigDBM.o -I inc/ $(INC_LIST)
obj/spConstants.o: src/spConstants.cpp
	g++ -m64 -Wall -g -c src/spConstants.cpp -o obj/spConstants.o -I inc/ $(INC_LIST)
obj/spBaseTreeManager.o: src/spBaseTreeManager.cpp
	g++ -m64 -Wall -g -c src/spBaseTreeManager.cpp -o obj/spBaseTreeManager.o -I inc/ $(INC_LIST)
obj/spJuniperParser.o: src/spJuniperParser.cpp
	g++ -m64 -Wall -g -c src/spJuniperParser.cpp -o obj/spJuniperParser.o -I inc/ $(INC_LIST)


clean:
	rm -rf obj/*.o
	rm -rf bin/spConfigParser