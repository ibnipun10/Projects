/* 
 * File:   spCiscoParser.h
 * Author: ccr
 *
 * Created on February 6, 2015, 4:55 PM
 */

#ifndef SPCISCOPARSER_H
#define	SPCISCOPARSER_H

#include "spBaseTree.h"
#include "spParser.h"

class CCiscoParser : public CParser
{
public:
    CBaseTree * CreateValueTree(string, CBaseTree*);
};

#endif	/* SPCISCOPARSER_H */

