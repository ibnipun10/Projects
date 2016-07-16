#include <iostream>

using namespace std;

#include "Controller.h"
#include "Board.h"



int main()
{

	
	cout << "\n Tic Tac Toe \n";
	char input;
	do
	{

		//get the game controller
		CController *objController = CController::getInstance();
		objController->InitializeBoard();

		//Enter player 1 details
		string Playername;
		cout << "Please enter player 1 name \n";
		cin >> Playername;

		CPlayer *playerA = CFactoryPlayer::GetPlayerInstance(Human, Playername);
		objController->InitializePlayers(PlayerA, playerA);

		cout << "Please enter player 2 name \n";
		cin >> Playername;
		CPlayer *playerB = CFactoryPlayer::GetPlayerInstance(Human, Playername);
		objController->InitializePlayers(PlayerB, playerB);

		objController->InitializeGame(HumanVsHuman);

		int iMove;

		while (objController->GetGameState() == GameRunning)
		{
			CPlayer *objPlayer = objController->GetPlayer();
			cout << objPlayer->GetName() << " your Turn \n";
			cin >> iMove;
			objPlayer->Move(iMove);

		}
		

		cout << "\n Do you want to play again (y)??";
		cin >> input;

	} while (input == 'Y' || input == 'y');
}