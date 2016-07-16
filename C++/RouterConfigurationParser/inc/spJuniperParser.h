/* 
 * File:   spJuniperParser.h
 * Author: root
 *
 * Created on February 16, 2015, 10:55 AM
 */

#ifndef SPJUNIPERPARSER_H
#define	SPJUNIPERPARSER_H
#include "spBaseTree.h"
#include "spParser.h"
class CJuniperParser : public CParser
{
public:
    CBaseTree * CreateValueTree(string, CBaseTree*);
};


#endif	/* SPJUNIPERPARSER_H */

