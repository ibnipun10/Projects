/* 
 * File:   spParser.h
 * Author: ccr
 *
 * Created on February 6, 2015, 4:55 PM
 */

#ifndef SPPARSER_H
#define	SPPARSER_H

#include "spBaseTree.h"

class CParser
{
public:
    CBaseTree* CreateCommandTree(string);
    virtual CBaseTree * CreateValueTree(string, CBaseTree*);
};

#endif	/* SPPARSER_H */

