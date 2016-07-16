#include "spBaseTree.h"
#include "spUtilities.h"

CBaseTree::CBaseTree(string value, CBaseTree* commandRoot)
{
    m_value = value;
    m_parent = NULL;
    m_nodeId = 0;
    m_link = commandRoot;
}


CBaseTree::CBaseTree(string value, int id, CBaseTree *commandRoot, char cStatus)
{
    m_value = value;
    m_nodeId = id;
    m_link = commandRoot;
    m_status = cStatus;
}

CBaseTree::~CBaseTree()
{
    
}

/*
 * Description : Add a child node
 * Param
 * @value : Value of the node
 * @key : Map key to which node is added
 * @nodeId : Node id of the node in the DB, if not in db it is 0
 * @commandRoot : Schema Tree node, for Schema nodes, this is NULL
 * @cStatus : 'A', 'D', 'R'
 */
CBaseTree* CBaseTree::addChild(string value, string key, int nodeId, CBaseTree* commandRoot, char cStatus)
{
    CBaseTree *childNode = new CBaseTree(value, nodeId, commandRoot, cStatus);
    childNode->m_parent = this;
    vector<CBaseTree*> vchilds;

    if(this->m_childrenMap.count(key) == 1)
        vchilds = this->m_childrenMap[key];
 
    vchilds.push_back(childNode);
    this->m_childrenMap[key] = vchilds;

    return childNode;
}

/*
 * This function will search for a child whose value starts in the line
 * @Line : Returns node whose value starts in the line
 */
CBaseTree* CBaseTree::hasChild(string line)
{

    map< string, vector< CBaseTree* > > vchilds = this->m_childrenMap;

    for(map< string, vector< CBaseTree* > >::iterator it = vchilds.begin(); it != vchilds.end(); it++)
    {
        vector< CBaseTree* > childs = (*it).second;
        for(vector< CBaseTree* >::iterator vit = childs.begin(); vit != childs.end(); vit++)
        {
            if(CUtilities::startsWith(line, (*vit)->getValue()))
                return *vit;
        }
    }

    return NULL;
}

/*
 * This function will search for a child with the same key and value
 * @key : Map key
 * @value : Value to be searched in the map
 */
CBaseTree* CBaseTree::hasChild(string key, string value)
{
    map< string, vector< CBaseTree* > > vchilds = this->m_childrenMap;

    map< string, vector< CBaseTree* > >::iterator it = vchilds.find(key);

    if(it != vchilds.end())
    {
        //key found
        vector< CBaseTree* > childs = (*it).second;
        for(vector< CBaseTree* >::iterator vit = childs.begin(); vit != childs.end(); vit++)
        {
            if((*vit)->getValue() == value)
                return (*vit);
        }
    }

    return NULL;
}



