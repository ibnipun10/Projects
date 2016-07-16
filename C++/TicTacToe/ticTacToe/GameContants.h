#include <string>
#include <iostream>

using namespace std;

class CCommon
{
public:
	static string ComputerNameA;
	static string ComputerNameB;
	static int PlayerAWin;
	static int PlayerBWin;
};



enum PlayerOptions
{
	HumanVsHuman,
	HumanVsComputer,
	ComputerVsComputer
};

enum eTypeOfPlayer
{
	Human,
	Computer
};

enum ePlayer
{
	PlayerA,
	PlayerB
};

enum eGameState
{
	GameWon,
	GameDraw,
	GameRunning
};

