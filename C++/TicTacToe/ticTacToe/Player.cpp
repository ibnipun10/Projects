#include "Player.h"
#include "Controller.h"

CPlayer::CPlayer(string name)
{
	m_Name = name;
}

void CHuman::Move(int iMove)
{
	CController *objController = CController::getInstance();
	objController->PlayMove(iMove);
}
