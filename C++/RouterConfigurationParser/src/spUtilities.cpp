#include "spUtilities.h"
#include <algorithm>
#include <stdio.h>
#include <stack>


int CUtilities::CountleadingSpaces(string line)
{
    int length = line.size();
    int count = 0;
    for(int i=0; i<length; i++)
    {
        if(line.at(i) == ' ')
            count++;
        else
            break;
    }

    return count;
}

// trim from start
string &CUtilities::ltrim(string &s) {
        s.erase(s.begin(), find_if(s.begin(), s.end(), not1(ptr_fun<int, int>(isspace))));
        return s;
}

// trim from end
string &CUtilities::rtrim(string &s) {
        s.erase(find_if(s.rbegin(), s.rend(), not1(ptr_fun<int, int>(isspace))).base(), s.end());
        return s;
}

// trim from both ends
string &CUtilities::trim(string &s) {
        return ltrim(rtrim(s));
}

bool CUtilities::startsWith(string &line, string startsWith)
{
    if(line.compare(0, startsWith.length(), startsWith) == 0)
        return true;
    else
        return false;
}
