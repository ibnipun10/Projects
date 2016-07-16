#include <iostream>
#include "PerformCalculations.h"
#include <fstream>
#include <string>

using namespace std;

void main()
{
	int nNumberofBuildings;
	string fileName = "Input.txt";
	string line;

	CPerformCalculations objPerformCalculations;
	
	ifstream infile(fileName);
	if (infile.is_open())
	{
		while (getline(infile, line))
		{
			objPerformCalculations.pushHeightArray(atoi(line.c_str()));
		}

		int retValue = objPerformCalculations.GetFinalXorValues();
		cout << "Here is the Output : " << retValue;
	}
	else
		cout << "Unable to open file " << fileName;

	

	
}