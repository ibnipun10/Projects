#include "Board.h"
#include "iostream"

using namespace std;


CBoard::CBoard()
{
}


CBoard::~CBoard()
{
}

void CBoard::DrawBoard(int mat[][3])
{
	cout << "\n\n";
	for (int i = 0; i < 3; i++)
	{
		cout << "-- -- -- \n";

		for (int j = 0; j < 3; j++)
		{			
			cout << "|";
					
			if (mat[i][j] == 0)
				cout << (i * 3 + j + 1);
			else if (mat[i][j] == 1)
				cout << "X";
			else cout << "0";
		}

		cout << "|";
		cout << "\n";
		
	}

	cout << "-- -- -- \n";

	cout << "\n\n";
}
