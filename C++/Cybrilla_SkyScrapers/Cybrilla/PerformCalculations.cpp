#include "PerformCalculations.h"
#include <algorithm>


CPerformCalculations::CPerformCalculations()
{
}


CPerformCalculations::~CPerformCalculations()
{
}

void CPerformCalculations::pushHeightArray(int height)
{
	heightArray.push_back(height);
}

void CPerformCalculations::popHightArray()
{
	heightArray.pop_back();
}

int CPerformCalculations::lenghtHeightArray()
{
	return heightArray.size();
}

void CPerformCalculations::SortHeightArray()
{
	sort(heightArray.begin(), heightArray.end());
}

int CPerformCalculations::CalculateNumberOfFloorToBuild(int M)
{
	if (M == 1)
		return 0;

	int length = lenghtHeightArray();
	int gSum = -1, lSum = 0;

	if (M > length)
		return 0;

	for (int i = 0; i < length - 1 ; i++)
	{
		int pos = i + M - 1;

		if (pos > length - 1)
			break;

		lSum = 0;
		for (int j = i; j < i + M - 1; j++)
		{
			lSum += heightArray.at(pos) - heightArray.at(j);
		}

		if (lSum == 0)
			return 0;
		else
		{
			if (gSum == -1)
				gSum = lSum;

			if (lSum < gSum)
				gSum = lSum;
		}
	}

	return gSum;
}

int CPerformCalculations::GetFinalXorValues()
{
	//Sort the array
	SortHeightArray();

	//Calcualte the values
	int iXorValue = 0;
	int tempValue;
	int length = lenghtHeightArray();

	for (int i = 2; i <= length; i++)
	{
		tempValue =  CalculateNumberOfFloorToBuild(i);
		iXorValue ^= tempValue;
	}

	return iXorValue;
}
