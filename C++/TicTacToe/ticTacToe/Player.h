#pragma once

#include "GameContants.h"

class CPlayer
{
private:
	string m_Name;
public:
	CPlayer(string);
	
	string GetName() { return m_Name; }
	virtual void Move(int) {};

};

class CHuman : public CPlayer 
{
public :
	CHuman(string name) : CPlayer(name)
	{

	}
	void Move(int);
};

class CComputer : public CPlayer
{
public :
	CComputer(string name) : CPlayer(name)
	{

	}
};

class CFactoryPlayer
{

public:
	static CPlayer* GetPlayerInstance(eTypeOfPlayer ePl, string PlayerName)
	{
		CPlayer *objPlayer = NULL;
		switch (ePl)
		{
		case Human : 
			objPlayer = new CHuman(PlayerName);
			break;
		case Computer :
			objPlayer = new CComputer(PlayerName);
			break;
		default : 
			break;
		}

		return objPlayer;
	}
};

