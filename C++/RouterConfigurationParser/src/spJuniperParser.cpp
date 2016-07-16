#include "spJuniperParser.h"
#include "spUtilities.h"
#include <fstream>
#include <iostream>
#include <stack>

CBaseTree * CJuniperParser::CreateValueTree(string ciscoConfigFile, CBaseTree *root) {
    CBaseTree *objValueRoot = new CBaseTree("I am value root", NULL);
    CBaseTree *objValueParent = objValueRoot;
    CBaseTree *objValueChild = NULL;
    CBaseTree *objCommandParent = root;
    CBaseTree *objCommandChild = NULL;
    CBaseTree *objCommandValueChild = NULL;
    string line, paramValue;
    stack<bool> stackbValue;
    stack<bool> stackbSection;
    stackbValue.push(false);
    stackbSection.push(true);
    
   // int count = 0;

    ifstream myfile(ciscoConfigFile.c_str());
    bool bValue = false;

    if (myfile.is_open()) {

        while (getline(myfile, line)) {
            if (line.empty()) {
                cout << "\n Getting Empty Line \n";
                continue;
            }
            
            line = CUtilities::trim(line);
            
            //get the last character
            char s1 = line.at(line.length() - 1);
            //get the line removing the last character
            line = line.substr(0, line.length() - 1);
            line = CUtilities::trim(line);
            
            
            switch(s1)
            {
                case '{' :  
                    
                    if(stackbSection.top())
                    {
                    
                        if(stackbValue.top())
                        {
                            stackbValue.push(false);
                            stackbSection.push(true);

                            paramValue = objCommandParent->getParent()->getValue();

                            objValueChild = objValueParent->addChild(line, paramValue, 0, objCommandParent->getParent(), 'A');
                            objValueParent = objValueChild;
                            
                            continue;                        
                        }
                    
                    
                        objCommandChild = objCommandParent->hasChild(line); 
                        
                        if(objCommandChild != NULL)
                        {
                            stackbSection.push(true);
                            objCommandValueChild = objCommandChild->hasChild("Value");
                            
                            if(objCommandValueChild != NULL)
                            {
                                objCommandParent = objCommandValueChild;
                                stackbValue.push(true);
                            }
                            else
                            {
                                stackbValue.push(false);
                                objCommandParent = objCommandChild;
                                objValueChild = objValueParent->addChild(line, objCommandParent->getValue(), 0, objCommandParent, 'A');
                                objValueParent = objValueChild;
                            }                            
                            
                        }
                        else
                        {
                            stackbSection.push(false);
                            
                        }
                    }
                    else
                    {
                        stackbSection.push(false);
                    }
                        break;
                case ';' : 
                    if(stackbSection.top())
                    {
                        if(stackbValue.top())
                        {
                            paramValue = objCommandParent->getParent()->getValue();

                            objValueChild = objValueParent->addChild(line, paramValue, 0, objCommandParent->getParent(), 'A');
                            continue;  
                        }
                        else
                        {
                             objCommandChild = objCommandParent->hasChild(line); 
                             if(objCommandChild != NULL)
                             {
                                  objValueChild = objValueParent->addChild(line, objCommandChild->getValue(), 0, objCommandChild, 'A');
                             }
                        }
                    }
                    break;
                case '}' : 
                    if(stackbSection.top())
                    {
                        if(!stackbValue.top())
                        {
                            stackbValue.pop();
                            
                            if(!stackbValue.top())
                            {
                                if(objCommandParent->getParent() != NULL)
                                        objCommandParent = objCommandParent->getParent();
                            }
                            
                        }
                        else
                            stackbValue.pop();
                        
                        if(objValueParent->getParent() != NULL)
                               objValueParent = objValueParent->getParent();                         
                         
                        
                    }
                    
                    stackbSection.pop();
                    
                   
                                        
                    break;
                default : continue;
            }
            
            
            
            
            
           
        }

    }

    myfile.close();
    return objValueRoot;
}