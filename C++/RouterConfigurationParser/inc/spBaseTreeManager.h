/* 
 * File:   spBaseTreeManager.h
 * Author: ccr
 *
 * Created on February 20, 2015, 10:50 AM
 */

#ifndef SPBASETREEMANAGER_H
#define	SPBASETREEMANAGER_H

#include <string>
#include "spBaseTree.h"
#include <iostream>
#include <fstream>

using namespace std;

class CBaseTreeManager
{
public:
    static void PrintTree(CBaseTree *, string);
    static void PrintTree(CBaseTree *, ofstream &, int );
    static CBaseTree * getNodeById(CBaseTree *, int);
    static CBaseTree* CompareValueTrees(CBaseTree *, CBaseTree *);
    static string getParamValueInLine(string, string );
    static void setTreeAllNodesStatus(CBaseTree *, char);
    static void DeleteBaseTree(CBaseTree *);
};

#endif	/* SPBASETREEMANAGER_H */

