/* 
 * File:   spUtilities.h
 * Author: ccr
 *
 * Created on February 6, 2015, 5:17 PM
 */

#ifndef SPUTILITIES_H
#define	SPUTILITIES_H

#include <string>

using namespace std;

class CUtilities
{
public:
    static int CountleadingSpaces(string);
    static string &ltrim(string &);
    static string &rtrim(string &);
    static string &trim(string &);
    static bool startsWith(string &, string );
};

#endif	/* SPUTILITIES_H */

