#include "Controller.h"

CController* CController::m_objControllerInstance = NULL;

CController::CController()
{
	
	InitializeBoard();
}

CController* CController::getInstance()
{
	if (m_objControllerInstance == NULL)
		m_objControllerInstance = new CController();
	
	return m_objControllerInstance;

}


void CController::InitializeBoard()
{
	memset(m_mat, 0, 9 * sizeof(int));
	memset(m_Col, 0, 3 * sizeof(int));
	memset(m_Row, 0, 3 * sizeof(int));

	m_diag1 = 0;
	m_diag2 = 0;
	m_move = false;

	m_gameState = GameRunning;
}

void CController::InitializePlayers(ePlayer player, CPlayer *objPlayer)
{
	switch (player)
	{
	case PlayerA : 
		m_plA = objPlayer;
		break;
	case PlayerB : 
		m_plB = objPlayer;
		break;
	default : 
		break;
	}

}

void CController::InitializeGame(PlayerOptions eplOption)
{
	m_ePlayOptions = eplOption;
	objboard.DrawBoard(m_mat);
}



bool CController::MapMove(int iNumber)
{
	if (iNumber < 0 || iNumber > 9)
	{
		cout << "Invalid move \n";
		return false;
	}
	else
	{
		int ctr = 0;
		while (iNumber > 3)
		{
			ctr++;
			iNumber -= 3;
		}
		m_plMoveRow = ctr;
		m_plMoveCol = iNumber - 1;

		return true;
	}
}

bool CController::ValidateMove(int iMove)
{
	if (!MapMove(iMove))
		return false;
	else
	{
		if (m_mat[m_plMoveRow][m_plMoveCol] != 0)
		{
			cout << "Invalid move \n";
			return false;
		}

	}

	return true;
}

bool CController::IsDiagonal(eDiagonal ediag)
{
	bool ret = false;

	switch (ediag)
	{
	case BackDiagonal :
		if (m_plMoveCol == m_plMoveRow)
			ret = true;
		break;
	case FrontDiagonal :
		if ((m_plMoveCol == 1 && m_plMoveRow == 1) || abs(m_plMoveCol - m_plMoveRow) == 2)
			ret = true;
		break;
	default: break;
	}

	return ret;
}

bool CController::IsWon(ePlayer playerT)
{
	bool ret = false;
	int iWining;
	switch (playerT)
	{
	case PlayerA : 
		iWining = CCommon::PlayerAWin;
			break;
	case PlayerB :
		iWining = CCommon::PlayerBWin;
		break;
	default: break;

	}

	if ((m_Col[m_plMoveCol]++) == iWining || (m_Row[m_plMoveRow]++) == iWining || m_diag1 == iWining || m_diag2 == iWining)
		return true;
	else
		return false;
	
}

bool CController::IsDraw()
{
	bool bDraw = true;

	for (int i = 0; i < 3; i++)
	{
		for (int j = 0; j < 3; j++)
		{
			if (m_mat[i][j] == 0)
			{
				bDraw = false;
				return bDraw;
			}
		}
	}

	return bDraw;
}

void CController::PlayMove(int iMove)
{
	if (!ValidateMove(iMove))
		return;

	if (!m_move)
	{
		//Player A
		//He wins for 3
		
		m_mat[m_plMoveRow][m_plMoveCol] = 1;

		if (IsDiagonal(BackDiagonal))
			m_diag1++;
		if (IsDiagonal(FrontDiagonal))
			m_diag2++;

		
		if (IsWon(PlayerA))
		{
			
			cout << GetPlayer()->GetName() << "Won the game \n";
			m_gameState = GameWon;
			return;
		}
		
	}
	else
	{
		//Player B
		//He wins for -3

		m_mat[m_plMoveRow][m_plMoveCol] = -1;

		if (IsDiagonal(BackDiagonal))
			m_diag1--;
		if (IsDiagonal(FrontDiagonal))
			m_diag2--;


		if (IsWon(PlayerB))
		{
			cout << GetPlayer()->GetName() << " Won the game \n ";
			m_gameState = GameWon;
			return;
		}
	}

	if (IsDraw())
	{
		cout << "Game Drawn \n ";
		m_gameState = GameDraw;
		return;
	}
	
	m_move = !m_move;

	objboard.DrawBoard(m_mat);
}

CPlayer* CController::GetPlayer()
{
	if (m_move)
		return m_plB;
	else
		return m_plA;
}