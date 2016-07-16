#include "spBaseTreeManager.h"
#include <algorithm>
#include <stdio.h>
#include <stack>
#include "spUtilities.h"


/*
 * Description : Prints the tree in the file
 * @root : Root pointer of the tree
 * @fileName : Full path of the file
 */
void CBaseTreeManager::PrintTree(CBaseTree *root, string fileName)
{
    //Print the base tree
    if(remove(fileName.c_str()) == 0)
        cout<<"File Deleted successfully";

    ofstream ofile(fileName.c_str());
    if(ofile.is_open())
    {
        PrintTree(root, ofile, 0);
        ofile.close();
    }
    else
        cout<<"Unable to open file for writing";
}


void CBaseTreeManager::PrintTree(CBaseTree *root, ofstream &offile, int space)
{
    if(root == NULL)
        return;
    else
    {
        if(root->getParent() != NULL)
        {
            string line = "";
            for(int i=0; i<space; i++)
                line += " ";

            line += root->getValue();
            offile<< line;
            offile<< '\n';
        }

        map< string, vector< CBaseTree* > > mchilds = root->getChildren();

        for(map< string, vector< CBaseTree* > >::iterator mit = mchilds.begin(); mit != mchilds.end(); mit++)
        {
            vector<CBaseTree*> vchilds = (*mit).second;

            string section = "";
            for(int i=0; i<space; i++)
                section += " ";

            string param = (*mit).first;
            int length = vchilds.size();
            section += "Section " + param;
            //offile<< section << "(" << length << ")";
            //offile<< '\n';


            for(vector<CBaseTree*>::iterator it = vchilds.begin(); it != vchilds.end(); it++)
            {
                PrintTree(*it, offile, space+1);
            }
        }
    }
}


/*
 * Description : Get the node passing the id of the node
 * @root : Pass the root of the tree
 * @nodeid : Pass the nodeid to be searched in the tree
 */
CBaseTree * CBaseTreeManager::getNodeById(CBaseTree *root, int nodeid)
{
    if(root == NULL)
        return NULL;
    if(root->getNodeid() == nodeid)
        return root;
    else
    {
        map< string, vector< CBaseTree* > > mchilds = root->getChildren();

        for(map< string, vector< CBaseTree* > >::iterator mit = mchilds.begin(); mit != mchilds.end(); mit++)
        {
            vector<CBaseTree*> vchilds = (*mit).second;
            for(vector<CBaseTree*>::iterator it = vchilds.begin(); it != vchilds.end(); it++)
            {
                CBaseTree* treeNode = getNodeById(*it, nodeid);
                if(treeNode != NULL)
                    return treeNode;
            }
        }
    }

    return NULL;
}

/*
 * Description : Returns the value part from the line having the parameter
 * @line : line
 * @param : parameter to be searched
 */
string CBaseTreeManager::getParamValueInLine(string line, string param)
{
    int start = param.length();

    string value = line.substr(start, string::npos);
    return CUtilities::trim(value);
}

/*
 * Description : Set status of all the nodes in the tree
 * @root : The root of the tree
 * @status : Pass status, either 'A', 'D', 'R'
 */
void CBaseTreeManager::setTreeAllNodesStatus(CBaseTree *root, char status)
{
    if(root == NULL)
        return;
    else
    {

        root->setNodeStatus(status);

        map< string, vector< CBaseTree* > > mchilds = root->getChildren();

        for(map< string, vector< CBaseTree* > >::iterator mit = mchilds.begin(); mit != mchilds.end(); mit++)
        {
            vector<CBaseTree*> vchilds = (*mit).second;

            for(vector<CBaseTree*>::iterator it = vchilds.begin(); it != vchilds.end(); it++)
            {
                setTreeAllNodesStatus(*it, status);
            }
        }
    }
}

/*
 * Description : Compare 2 trees.
 * @oldRoot : Root of one tree
 * @newRoot : Root of the other tree
 */
CBaseTree * CBaseTreeManager::CompareValueTrees(CBaseTree *oldRoot, CBaseTree *newRoot)
{
    CBaseTree* updatedRoot = oldRoot;
    stack<CBaseTree*> nodeQueue;
    stack<int> noOfChilds;
    nodeQueue.push(newRoot);
    noOfChilds.push(1);
    int iChilds;
    int ichildrenCount;

    string query;
    int count = 0;

    while( !nodeQueue.empty() )
    {
        ichildrenCount = 0;
        CBaseTree* node = nodeQueue.top();
        nodeQueue.pop();
        iChilds = noOfChilds.top();


        if(iChilds == 0)
        {
            do
            {
                noOfChilds.pop();
                iChilds = noOfChilds.top();
                oldRoot = oldRoot->getParent();
            }while(iChilds == 0);
        }

        noOfChilds.pop();
        noOfChilds.push(iChilds - 1);


        if(node == NULL)
            continue;
        else
        {
            if(node->getParent() != NULL)
            {
                oldRoot = oldRoot->hasChild(node->getSchemaLink()->getValue(), node->getValue());
            }

            map< string, vector< CBaseTree* > > mchilds = node->getChildren();

            if(mchilds.size() == 0)
            {
                noOfChilds.push(0);
                continue;
            }

            for(map< string, vector< CBaseTree* > >::iterator mit = mchilds.begin(); mit != mchilds.end(); mit++)
            {
                vector<CBaseTree*> vchilds = (*mit).second;

                ichildrenCount += vchilds.size();

                for(vector<CBaseTree*>::iterator it = vchilds.begin(); it != vchilds.end(); it++)
                {
                    CBaseTree* newtreenode = *it;
                    if(newtreenode == NULL)
                        continue;

                    CBaseTree* oldTreeChild = oldRoot->hasChild(newtreenode->getSchemaLink()->getValue(), newtreenode->getValue());
                    if(oldTreeChild == NULL)
                    {
                        oldRoot->addChild(newtreenode->getValue(), newtreenode->getSchemaLink()->getValue(), 0, newtreenode->getSchemaLink(), 'A');

                    }
                    else
                    {
                        oldTreeChild->setNodeStatus('R');
                    }

                    nodeQueue.push(*it);
                }

            }

            noOfChilds.push(ichildrenCount);
        }
    }

    return updatedRoot;
}

/*
 * Description : Deletes a tree
 * @root : Root of the tree
 */
void CBaseTreeManager::DeleteBaseTree(CBaseTree *root)
{
     if(root == NULL)
        return;
    else
    {
        map< string, vector< CBaseTree* > > mchilds = root->getChildren();

        for(map< string, vector< CBaseTree* > >::iterator mit = mchilds.begin(); mit != mchilds.end(); mit++)
        {
            vector<CBaseTree*> vchilds = (*mit).second;

            for(vector<CBaseTree*>::iterator it = vchilds.begin(); it != vchilds.end(); it++)
            {
                DeleteBaseTree(*it);
            }
        }

        delete root;
    }
}


