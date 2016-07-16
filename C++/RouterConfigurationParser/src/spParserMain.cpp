#include "spCiscoParser.h"
#include "spUtilities.h"
#include "spConfigDBM.h"
#include "spBaseTreeManager.h"
#include "spJuniperParser.h"


spDBMForConfig objDBMConfig("ConfigDB", "root", "");

CBaseTree* ReadSchemaTreeFromFile(string file, CParser *root)
{
    cout<<"\n Read Schema Tree from file";
    
    CBaseTree *objCommandRoot = root->CreateCommandTree(file);
    
    return objCommandRoot;
}

void WriteSchemaTreeToDB(CBaseTree *root)
{
    cout<< "\n Write Schema Tree from DB";
    objDBMConfig.WriteSchemaTreeToDB(root);
}

CBaseTree* ReadSchemaTreeFromDB()
{
    cout<< "\n Read schema Tree from DB";
    return objDBMConfig.ReadSchemaTreeFromDB();
}

void WriteValueTreeToDB(CBaseTree* root)
{
    cout<< "\n Write Value tree from DB";
    objDBMConfig.WriteValueTreeToDB(root);
}

CBaseTree* ReadValueTreeFromDB(CBaseTree* objCommand)
{
    cout <<"\n Read value tree from DB";
    return objDBMConfig.ReadValueTreeFromDB(objCommand);
}

CBaseTree* ReadValueTreeFromFile(CBaseTree* objCommand, string file, CParser *root)
{
    cout <<"\n Read value tree from file";
    
    return root->CreateValueTree(file, objCommand);

}

CBaseTree* Compare2trees(CBaseTree* root1, CBaseTree* root2)
{

    cout<<"\n Compare 2 trees";
    return CBaseTreeManager::CompareValueTrees(root1, root2);
}

void WriteValueTreeToFile(CBaseTree* root, string file)
{
    cout << "\n Write value tree to file";
    CBaseTreeManager::PrintTree(root, file);
}


int main(int argc, char *argv[])
{
    CBaseTree* oldValue = NULL;
    CBaseTree* command = NULL;
    CBaseTree* valueRoot = NULL;
    CBaseTree* newRoot = NULL;
    CParser *root = new CJuniperParser();


    
    
       command = ReadSchemaTreeFromFile("Resources//juniperconfigschema.txt", root);
       WriteSchemaTreeToDB(command);

        command = ReadSchemaTreeFromDB();
/*
        oldValue = ReadValueTreeFromFile(command, "Resources//CiscoConfig1.txt");
        WriteValueTreeToDB(oldValue);
*/
    //    oldValue = ReadValueTreeFromDB(command);
        newRoot = ReadValueTreeFromFile(command, "Resources//182.19.96.234.config.txt", root);


   //     CBaseTreeManager::setTreeAllNodesStatus(oldValue, 'D');
   //     valueRoot = Compare2trees(oldValue, newRoot);
       // WriteValueTreeToFile(newRoot, "valueTree1.txt");
        WriteValueTreeToDB(newRoot);
 
}