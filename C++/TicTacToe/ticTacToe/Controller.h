#pragma once


#include "Player.h"
#include "Board.h"

enum eDiagonal
{
	BackDiagonal,
	FrontDiagonal
};



/* this is a singleton class */
class CController
{
private :
	static CController *m_objControllerInstance;
	CController();
	int m_mat[3][3];
	int m_Row[3], m_Col[3], m_diag1, m_diag2;
	CPlayer *m_plA, *m_plB;
	PlayerOptions m_ePlay;
	bool m_move;
	int m_plMoveRow, m_plMoveCol;
	eGameState m_gameState;
	PlayerOptions m_ePlayOptions;
	CBoard objboard;

	//Methods
	bool MapMove(int);
	bool IsDiagonal(eDiagonal);
	bool IsWon(ePlayer);
	bool IsDraw();
	
	bool ValidateMove(int);
public:
	static CController* getInstance();
	~CController();

	

	void InitializeBoard();
	void InitializeGame(PlayerOptions);
	void InitializePlayers(ePlayer, CPlayer*);
	void PlayMove(int);
	CPlayer* GetPlayer();
	eGameState GetGameState() { return m_gameState; }
};




