#pragma once
#include <vector>
using namespace std;
class CPerformCalculations
{
private :
	vector<int> heightArray;
	void SortHeightArray();

public:
	CPerformCalculations();
	~CPerformCalculations();

	void pushHeightArray(int);
	void popHightArray();
	int lenghtHeightArray();

	int CalculateNumberOfFloorToBuild(int);
	int GetFinalXorValues();
};

