#include "spParser.h"
#include <fstream>
#include "spUtilities.h"
#include <iostream>

CBaseTree* CParser::CreateCommandTree(string schemaFile)
{
    CBaseTree *root = new CBaseTree("I am command root", NULL);
    CBaseTree *parent = root;
    CBaseTree *child = NULL;

    
    string line;
    int space = 0;

    ifstream myfile(schemaFile.c_str());
    if(myfile.is_open())
    {
        while(getline(myfile, line))
        {
            int lineSpace = CUtilities::CountleadingSpaces(line);
            line = CUtilities::trim(line);

            if(line.empty())
                continue;

            if(space == lineSpace)
            {
                child = parent->addChild(line, line);
            }
            else if(lineSpace > space)
            {
                parent = child;
                child = child->addChild(line, line);
                space++;
            }
            else if(lineSpace < space)
            {
                for(int i=0; i<(space - lineSpace); i++)
                    parent = parent->getParent();

                child = parent->addChild(line, line);
                space = lineSpace;
            }

        }
        myfile.close();
    }
    else
        cout<<"Unable to open CommandTree schema file";


    return root;
}

CBaseTree * CParser::CreateValueTree(string ciscoConfigFile, CBaseTree *root)
{
    cout<< "Please call respective parser function";
}
