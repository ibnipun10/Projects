#include "spCiscoParser.h"
#include "spUtilities.h"
#include "spBaseTreeManager.h"
#include <fstream>
#include <iostream>


/*
 * Description : Create value tree from the file
 * @ciscoConfigFile : File path
 * @root : Root of the tree
 */
CBaseTree * CCiscoParser::CreateValueTree(string ciscoConfigFile, CBaseTree *root)
{
    CBaseTree *objValueRoot = new CBaseTree("I am value root", NULL);
    CBaseTree *objValueParent = objValueRoot;
    CBaseTree *objValueChild = NULL;

    CBaseTree *objCommandParent = root;
    CBaseTree *objCommandChild = NULL;

    string line, paramValue, lineCopy;
    int space = 0;
    bool bSection = true;


    ifstream myfile(ciscoConfigFile.c_str());
    if(myfile.is_open())
    {
        while(getline(myfile, line))
        {
            int lineSpace = CUtilities::CountleadingSpaces(line);

            lineCopy = line;
            line = CUtilities::trim(line);
            if(CUtilities::startsWith(line, "!"))
                continue;


            if(lineSpace == space)
            {
                objCommandChild = objCommandParent->hasChild(line);

                if(objCommandChild != NULL)
                {
                    paramValue = CBaseTreeManager::getParamValueInLine(line, objCommandChild->getValue());

                    objValueChild = objValueParent->addChild(paramValue, objCommandChild->getValue(), 0, objCommandChild, 'A');
                    bSection = true;
                }
                else
                {
                    
                    bSection = false;
                }

            }
            else if(lineSpace > space && bSection)
            {
                objCommandParent = objCommandChild;
                objCommandChild = objCommandParent->hasChild(line);
                objValueParent = objValueChild;

                if(objCommandChild != NULL)
                {
                    paramValue = CBaseTreeManager::getParamValueInLine(line, objCommandChild->getValue());
                    objValueChild = objValueParent->addChild(paramValue, objCommandChild->getValue(), 0, objCommandChild, 'A');
                    bSection = true;
                }
                else
                {                    
                    bSection = false;
                }

                space++;
            }
            else if(lineSpace < space)
            {
                for(int i=0; i< space - lineSpace; i++)
                {
                    if(objCommandParent->getParent() != NULL)
                        objCommandParent = objCommandParent->getParent();

                    if(objValueParent->getParent() != NULL)
                        objValueParent = objValueParent->getParent();
                }

                objCommandChild = objCommandParent->hasChild(line);
                if(objCommandChild != NULL)
                {
                    paramValue = CBaseTreeManager::getParamValueInLine(line, objCommandChild->getValue());
                    objValueChild = objValueParent->addChild(paramValue, objCommandChild->getValue(), 0, objCommandChild, 'A');
                    bSection = true;
                }
                else
                    bSection = false;

                space = lineSpace;
            }
        }
        myfile.close();
    }
    else
        cout<<"Unable to open CommandTree schema file";

    return objValueRoot;
}
